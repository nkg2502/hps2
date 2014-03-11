package tsj.hps;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) 
	{
		
		// initialize main frame
		final JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Human Predator System : Testing camouflage using digital photograhps");
		mainFrame.setSize(1800, 800);
		mainFrame.setBackground(Color.BLACK);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initialize experiment dialog
		final JDialog experimentDialog = new JDialog(mainFrame);
		experimentDialog.setSize(400, 100);
		experimentDialog.setLayout(new FlowLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		experimentDialog.setLocation((screenSize.width - 400) / 2, (screenSize.height - 100) / 3);
	
		// read directories
		File[] directories = new File(".").listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname)
			{
				return pathname.isDirectory();
			}
		});
		
		// initialize background combobox
		final JComboBox backgroundComboBox = new JComboBox();
		backgroundComboBox.setEditable(false);
		
		// initialize target combobox
		final JComboBox targetComboBox = new JComboBox();
		targetComboBox.setEditable(false);
		
		for(File i: directories) {
			backgroundComboBox.addItem(i);
			targetComboBox.addItem(i);
		}
		
		// initialize ok button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Manager manager = Manager.getInstance();
				
				manager.setBackgroundPath((File) backgroundComboBox.getSelectedItem());
				manager.setTargetPath((File) targetComboBox.getSelectedItem());
				manager.setAge(26);
				manager.setGender(Manager.FEMALE);
				manager.setShowTimeInterval(5000);
				manager.setBreakTimeInterval(1000);
				
				// hide dialog
				experimentDialog.setVisible(false);
				
				try {
					mainFrame.add(new ImageViewer(manager.getShowTimeInterval(), 
							manager.getBreakTimeInterval(), 
							new Dispatcher(manager.getBackgroundPath(), 
									manager.getTargetPath())));
				} catch(Exception e) {
					e.printStackTrace();
				}
					
				// start experiment
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(mainFrame);
				mainFrame.setVisible(true);
			}
		});
		
		// add panel
		JPanel experimentPanel = new JPanel();
		
		experimentPanel.add(backgroundComboBox);
		experimentPanel.add(targetComboBox);
		experimentPanel.add(okButton);
		
		experimentDialog.add(experimentPanel);
		
		experimentDialog.setVisible(true);

	}

}
