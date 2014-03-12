package tsj.hps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import javax.swing.JPanel;

import tsj.hps.ds.ExperimentData;

/**
 * ImageViewer
 * 
 * @author Taesung Jung
 *
 */
public class ImageViewer extends JPanel {

	/**
	 * Secret signature
	 */
	private static final long serialVersionUID = 19891020200000L;

	private Dispatcher dispatcher;
	
	private Image backgroundImage;
	private Rectangle2D backgroundArea;
	private String backgroundName;
	
	private Image targetImage;
	private Rectangle2D targetArea;
	private String targetName;
	
	private long startTime;

	private Timer showTimer;
	
	private Timer breakTimer;
	
	private boolean isShowTime;
	
	private Robot robot;
	private Cursor blackCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "BLANK_CURSOR");
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**
	 * 
	 * @param showTimeInterval
	 * @param breakTimeInterval
	 * @param dispatcher
	 */
	public ImageViewer(int showTimeInterval, int breakTimeInterval, Dispatcher dispatcher) throws AWTException {
		
		this.dispatcher = dispatcher;
		this.robot = new Robot();
		
		isShowTime = false;
		
		// add mouse listener
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent event) {
			
				// only valid left mouse
				if(isShowTime && MouseEvent.BUTTON1 == event.getButton()) 
					// only valid in background 
					if(backgroundArea.contains(event.getPoint()))
						verify(event.getPoint());
			}
		});
		
		// add keyboard listener
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent event)
			{
				if(isShowTime && KeyEvent.VK_SPACE == event.getKeyCode())
					verify(false, true);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		// Initialize show timer
		this.showTimer = new Timer(showTimeInterval, new ActionListener() {
			
			/**
			 * Invoked show timeout
			 */
			@Override
			public void actionPerformed(ActionEvent event) {
				
				showTimer.stop();
				isShowTime = false;
				
				verify(false, false);
			}
		});
		
		// Initialize break timer
		this.breakTimer = new Timer(breakTimeInterval, new ActionListener() {
			
			/**
			 * Invoked break timeout
			 */
			@Override
			public void actionPerformed(ActionEvent event) {
				
				breakTimer.stop();
				
				nextImage();
			
				isShowTime = true;
				showTimer.start();
				
				startTime = (new GregorianCalendar()).getTimeInMillis();
			}
		});
		
		this.setFocusable(true);
		
		// experiment start!
		breakTimer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// clear screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		
		// hide mouse pointer
		this.setCursor(blackCursor);
		
		if(isShowTime) {
			
			// show mouse pointer
			robot.mouseMove((int) backgroundArea.getX() - 100, (int) backgroundArea.getY() - 100);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			// draw background image
			g.drawImage(backgroundImage, (int) backgroundArea.getX(), (int) backgroundArea.getY(), this);
			g.drawImage(targetImage, (int) targetArea.getX(), (int) targetArea.getY(), this);
			
			// FOR DEBUGGING
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(Color.WHITE);
			g2.draw(backgroundArea);
			g2.draw(targetArea);
			
			// draw target image
			
			
			
		}
	}
	
	private void verify(Point point) {
		verify(targetArea.contains(point), false);
	}
	
	private void verify(boolean isFound, boolean isPassed) {
		
		isShowTime = false;
		
		showTimer.stop();
		
		breakTimer.start();
		
		ExperimentData data = new ExperimentData();
		data.setBackgroundName(backgroundName);
		data.setTargetName(targetName);
		data.setFound(isFound);
		data.setPassed(isPassed);
		data.setTime(startTime);
		
		dispatcher.addExperimentData(data);
		
		this.repaint();
	}
	
	private void nextImage() {
		
		File backgroundFile = dispatcher.popBackgroundImage();
		File targetFile = dispatcher.popTargetImage();
		
		// TODO: targetFile 없으면 어쩔꺼?
		if(null == backgroundFile)
			System.exit(0);
		
		backgroundName = backgroundFile.getName();
		targetName = targetFile.getName();
		
		backgroundImage = loadImage(backgroundFile);
		targetImage = loadImage(targetFile);
				
		int rx = (screenSize.width - backgroundImage.getWidth(this)) / 2;
		int ry = (screenSize.height - backgroundImage.getHeight(this)) / 2;
		
		backgroundArea = new Rectangle2D.Double(rx, ry, 
				backgroundImage.getWidth(this),
				backgroundImage.getHeight(this));
				
		targetArea = new Rectangle2D.Double(rx, ry,
				10,
				10);

		this.repaint();
	}
	
	private Image loadImage(File imageFile) {
		
		if(null == imageFile)
			return null;
		
		Image loadedImage = null;
		try {
			loadedImage = ImageIO.read(imageFile);
		} catch(IOException e) {
			// TODO: more information
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return loadedImage;
	}

	
}
