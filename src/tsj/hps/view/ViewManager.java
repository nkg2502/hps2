package tsj.hps.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.PrettyNamedFile;
import tsj.hps.model.DataManager;
import tsj.hps.model.Dispatcher;
import tsj.hps.model.RandomDispatcher;
import tsj.hps.model.ReplayDispatcher;

/**
 * Manage all frame and dialog.
 * 
 * @author Taesung Jung
 *
 */
public class ViewManager implements Observer {
	

	private final static ViewManager instance = new ViewManager();
	
	private JFrame mainFrame = new JFrame();
	
	private ViewManager() {}
	
	public static ViewManager getInstance() {
		return instance;
	}
	
	public void init() {
		mainFrame.setTitle("Human Predator System : Testing camouflage using digital photograhps");
		mainFrame.setSize(1800, 800);
		mainFrame.setBackground(Color.BLACK);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * initialize experiment dialog
	 * 
	 * @param predefinedShowTimeInterval
	 * 	show time interval
	 * @param predefinedBreakTimeInterval
	 * 	break time interval
	 * @param predefinedAge 
	 * 	age
	 */
	public void experimentDialog(String predefinedShowTimeInterval, String predefinedBreakTimeInterval, String predefinedAge) {
		
		final JDialog experimentDialog = new JDialog(mainFrame);
		experimentDialog.setTitle("Human Predator System : Testing camouflage using digital photograhps");
		experimentDialog.setSize(400, 400);
		experimentDialog.setLayout(new FlowLayout());
		experimentDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		experimentDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) { }
			
			@Override
			public void windowIconified(WindowEvent e) { }
			
			@Override
			public void windowDeiconified(WindowEvent e) { }
			
			@Override
			public void windowDeactivated(WindowEvent e) { }
			
			@Override
			public void windowClosing(WindowEvent e) { }
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0); 
			}
			
			@Override
			public void windowActivated(WindowEvent e) { }
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		experimentDialog.setLocation((screenSize.width - 400) / 2, (screenSize.height - 100) / 3);
	
		// Initialize labels
		JLabel backgroundImageFolder = new JLabel("Background Image Folder");
		JLabel targetImageFolder = new JLabel("Target Image Folder");
		JLabel age = new JLabel("Age");
		JLabel gender = new JLabel("Gender");
		JLabel showTimeInterval = new JLabel("Show Time Interval(Unit : miliseconds)");
		JLabel breakTimeInterval = new JLabel("Break Time Interval(Unit : miliseconds)");
		
		final JComboBox backgroundComboBox = new JComboBox();
		backgroundComboBox.setEditable(false);
		
		final JComboBox targetComboBox = new JComboBox();
		targetComboBox.setEditable(false);
		
		// read directories
		File[] directories = new File(".").listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		for(File i: directories) {
			backgroundComboBox.addItem(new PrettyNamedFile(i, ""));
			targetComboBox.addItem(new PrettyNamedFile(i, ""));
		}
		
		final JTextField ageField = new JTextField(4);
		ageField.setText((String) (null != predefinedAge
				? predefinedAge : ""));
		
		final JTextField showTimeField = new JTextField(6);
		showTimeField.setText((String) (null != predefinedShowTimeInterval 
				? predefinedShowTimeInterval : ""));
	
		final JTextField breakTimeField = new JTextField(6);
		breakTimeField.setText((String) (null != predefinedBreakTimeInterval 
				? predefinedBreakTimeInterval : ""));
		
		final JRadioButton femaleRadioButton = new JRadioButton("Female", true);
		final JRadioButton maleRadioButton = new JRadioButton("Male", false);
		
		final ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(femaleRadioButton);
		genderGroup.add(maleRadioButton);
		
		final JButton startButton = new JButton("          Start          ");
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event)
			{
				DataManager dataManager = DataManager.getInstance();
				
				try {
					dataManager.setBackgroundPath((File) backgroundComboBox.getSelectedItem());
					dataManager.setTargetPath((File) targetComboBox.getSelectedItem());
					dataManager.setAge(Integer.parseInt(ageField.getText()));
					dataManager.setGender(femaleRadioButton.isSelected() ? DataManager.FEMALE : DataManager.MALE);
					dataManager.setShowTimeInterval(Integer.parseInt(showTimeField.getText()));
					dataManager.setBreakTimeInterval(Integer.parseInt(breakTimeField.getText()));
					
				} catch(NumberFormatException e) {
					e.printStackTrace();
					return;
					
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
				// hide dialog
				experimentDialog.setVisible(false);
				
				try {
					Dispatcher dispatcher = new RandomDispatcher(dataManager.getBackgroundPath(), dataManager.getTargetPath());
					dispatcher.addObserver(dataManager);
					dispatcher.addObserver(ViewManager.getInstance());
					
					mainFrame.add(new ImageViewer(dataManager.getShowTimeInterval(), 
							dataManager.getBreakTimeInterval(), 
							dispatcher,
							1020 == dataManager.getAge()));
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
					
				// start experiment
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(mainFrame);
				mainFrame.setVisible(true);
			}
		});
		
		// set up dialog
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, backgroundImageFolder, backgroundComboBox));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, targetImageFolder, targetComboBox));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.X_AXIS, age, ageField));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.X_AXIS, gender, femaleRadioButton, maleRadioButton));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, showTimeInterval, showTimeField));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, breakTimeInterval, breakTimeField));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(startButton);
	
		experimentDialog.add(panel);
		experimentDialog.setVisible(true);
	}
	
	public void resultDialog(List<ExperimentData> resultList) {

		final JDialog resultDialog = new JDialog(mainFrame);
		resultDialog.setTitle("Human Predator System : Result");
		resultDialog.setSize(400, 400);
		resultDialog.setLayout(new FlowLayout());
		resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		resultDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) { }
			
			@Override
			public void windowIconified(WindowEvent e) { }
			
			@Override
			public void windowDeiconified(WindowEvent e) { }
			
			@Override
			public void windowDeactivated(WindowEvent e) { }
			
			@Override
			public void windowClosing(WindowEvent e) { }
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0); 
			}
			
			@Override
			public void windowActivated(WindowEvent e) { }
		});
	
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		resultDialog.setLocation((screenSize.width - 300) / 2, (screenSize.height - 100) / 3);
		
		int[] statusSummary = DataManager.summarizeStatus(resultList);
		long[] timeSummary = DataManager.summarizeTime(resultList);
		
		JLabel summary = new JLabel("Summary");
		summary.setFont(new Font(null, Font.BOLD, 30));
		
		JLabel timeSpent = new JLabel("Time Spent ");
		timeSpent.setFont(new Font(null, Font.PLAIN, 25));
		
		JLabel user = new JLabel("User");
		user.setFont(new Font(null, Font.PLAIN, 25));
		
		JLabel timeTotal = new JLabel("                Total: " + TIME_SUMMARY_FORMAT(timeSummary[DataManager.TIME_TOTAL]));
		timeTotal.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel timeUsed = new JLabel("                Used: " + TIME_SUMMARY_FORMAT(timeSummary[DataManager.TIME_USED]));
		timeUsed.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel timeAverage = new JLabel("           Average: " + TIME_SUMMARY_FORMAT((long) ((float) timeSummary[DataManager.TIME_USED] / (resultList.size()))));
		timeAverage.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel frog = new JLabel("              Found: " + USER_SUMMARY_FORMAT(statusSummary[DataManager.USER_FOUND], resultList.size()));
		frog.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel passed = new JLabel("            Passed: " + USER_SUMMARY_FORMAT(statusSummary[DataManager.USER_PASSED], resultList.size()));
		passed.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel missClicked = new JLabel("    Miss Clicked: " + USER_SUMMARY_FORMAT(statusSummary[DataManager.USER_MISS_CLICKED], resultList.size()));
		missClicked.setFont(new Font(null, Font.PLAIN, 15));
		
		JLabel timeout = new JLabel("           Timeout: " + USER_SUMMARY_FORMAT(statusSummary[DataManager.USER_TIMEOUT], resultList.size()));
		timeout.setFont(new Font(null, Font.PLAIN, 15));
		
		// set up dialog
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		panel.add(summary);
		panel.add(timeSpent);
		panel.add(timeTotal);
		panel.add(timeUsed);
		panel.add(timeAverage);
		panel.add(user);
		panel.add(frog);
		panel.add(passed);
		panel.add(missClicked);
		panel.add(timeout);
		
		resultDialog.add(panel);
		resultDialog.setVisible(true);
	}
	
	public void replyDialog() {

				try {
					Dispatcher dispatcher = new ReplayDispatcher(null);
					
					mainFrame.add(new ImageViewer(10000, 300, dispatcher, true));

				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
					
				// start replay
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(mainFrame);
				mainFrame.setVisible(true);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		
		// hide main frame
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		mainFrame.setVisible(false);
		
		resultDialog((List<ExperimentData>) arg);
	}
	
	private static JPanel generateBoxPanel(int axis, JComponent... args) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		for(JComponent i: args)
			panel.add(i);
		
		return panel;
	}
	
	private static String USER_SUMMARY_FORMAT(int numerator, int denominator) {
		return String.format("%3d / %3d = %3.2f%%", numerator, denominator, (float) 100 * numerator / denominator);
	}
	
	private static String TIME_SUMMARY_FORMAT(long time) {
		return String.format("%.4f seconds", (float) time / 1000);
	}
	
}
