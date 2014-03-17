package tsj.hps.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import tsj.hps.model.DataManager;
import tsj.hps.model.Dispatcher;

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
	 */
	public void experimentDialog(String predefinedShowTimeInterval, String predefinedBreakTimeInterval) {
		
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
		
		// FIXME: added . + directory name
		for(File i: directories) {
			backgroundComboBox.addItem(i);
			targetComboBox.addItem(i);
		}
		
		final JTextField ageField = new JTextField(4);
		
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
		
		final JButton startButton = new JButton("       Start       ");
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
					Dispatcher dispatcher = new Dispatcher(dataManager.getBackgroundPath(), dataManager.getTargetPath());
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
	
	// FIXME: modify UI
	public void resultDialog() {
		
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
		resultDialog.setLocation((screenSize.width - 400) / 2, (screenSize.height - 100) / 3);
	
		// Initialize labels
		JLabel backgroundImageFolder = new JLabel("Background Image Folder");
		JLabel targetImageFolder = new JLabel("Target Image Folder");
		JLabel age = new JLabel("Age");
		JLabel gender = new JLabel("Gender");
		JLabel showTimeInterval = new JLabel("Show Time Interval(Unit : miliseconds)");
		JLabel breakTimeInterval = new JLabel("Break Time Interval(Unit : miliseconds)");
	
	
		final JButton startButton = new JButton("       Exit       ");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		// set up dialog
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(generateBoxPanel(BoxLayout.Y_AXIS, new JLabel(" ")));
		panel.add(startButton);
	
		resultDialog.add(panel);
		resultDialog.setVisible(true);
	}
	
	private static JPanel generateBoxPanel(int axis, JComponent... args) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		for(JComponent i: args)
			panel.add(i);
		
		return panel;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		mainFrame.setVisible(false);
		
		resultDialog();
	}
}
