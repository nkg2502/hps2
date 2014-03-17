package tsj.hps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

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

public class Main {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args)
	{
		init();
	}

	public static void init() {
		
		final JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Human Predator System : Testing camouflage using digital photograhps");
		mainFrame.setSize(1800, 800);
		mainFrame.setBackground(Color.BLACK);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initialize experiment dialog
		final JDialog experimentDialog = new JDialog(mainFrame);
		experimentDialog.setTitle("Human Predator System : Testing camouflage using digital photograhps");
		experimentDialog.setSize(400, 400);
		experimentDialog.setLayout(new FlowLayout());
		
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
			backgroundComboBox.addItem(i);
			targetComboBox.addItem(i);
		}
		
		final JTextField ageField = new JTextField(4);
		
		final JTextField showTimeField = new JTextField(6);
		final JTextField breakTimeField = new JTextField(6);
		
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
				Manager manager = Manager.getInstance();
				
				try {
					manager.setBackgroundPath((File) backgroundComboBox.getSelectedItem());
					manager.setTargetPath((File) targetComboBox.getSelectedItem());
					manager.setAge(Integer.parseInt(ageField.getText()));
					manager.setGender(femaleRadioButton.isSelected() ? Manager.FEMALE : Manager.MALE);
					manager.setShowTimeInterval(Integer.parseInt(showTimeField.getText()));
					manager.setBreakTimeInterval(Integer.parseInt(breakTimeField.getText()));
					
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
					mainFrame.add(new ImageViewer(manager.getShowTimeInterval(), 
							manager.getBreakTimeInterval(), 
							manager.getDispatcher(),
							1020 == manager.getAge()));
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
	
	private static JPanel generateBoxPanel(int axis, JComponent... args) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		for(JComponent i: args)
			panel.add(i);
		
		return panel;
	}
}