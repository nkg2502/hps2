package tsj.hps.view;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.ImageNode;
import tsj.hps.model.Dispatcher;

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
	
	/**
	 * For debugging
	 */
	private boolean godMode = false;

	private Dispatcher dispatcher;
	
	private Image backgroundImage;
	private Rectangle2D backgroundArea;
	private String backgroundName;
	
	private Image targetImage;
	private Rectangle2D targetArea;
	private String targetName;
	private int targetX;
	private int targetY;
	
	private long startTime;

	private Timer showTimer;
	
	private Timer breakTimer;
	
	private boolean isShowTime;
	
	private Robot robot;
	private Cursor blackCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "BLANK_CURSOR");
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final ActionListener nullActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {}
	};
	
	/**
	 * draw background image and target image
	 * 
	 * @param showTimeInterval
	 * @param breakTimeInterval
	 * @param dispatcher
	 */
	public ImageViewer(int showTimeInterval, int breakTimeInterval, Dispatcher dispatcher, boolean godMode) throws AWTException {
		
		this.dispatcher = dispatcher;
		this.godMode = godMode;
		
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
			
			// draw target image
			g.drawImage(targetImage, (int) targetArea.getX(), (int) targetArea.getY(), this);
			
			// for debugging
			if(godMode) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setPaint(Color.WHITE);
				g2.draw(backgroundArea);
				g2.draw(targetArea);
			}
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
		data.setTargetPoint(targetX, targetY);
		
		// added 5 due to timer difference
		data.setTime((new GregorianCalendar()).getTimeInMillis() - startTime + 5);
		
		dispatcher.addExperimentData(data);
		
		this.repaint();
	}
	
	private void nextImage() {
		
		ImageNode imageNode = dispatcher.popImage();
		
		// end experiment
		if(null == imageNode) {
			
			endExperiment();
			return;
		}
		
		File backgroundFile = imageNode.getBackgroundImage();
		File targetFile = imageNode.getTargetImage();
		
		backgroundName = backgroundFile.getName();
		targetName = targetFile.getName();
		
		backgroundImage = loadImage(backgroundFile);
		targetImage = loadImage(targetFile);
				
		int topX = (screenSize.width - backgroundImage.getWidth(this)) / 2;
		int topY = (screenSize.height - backgroundImage.getHeight(this)) / 2;
		
		try {
			targetX = new Random(System.nanoTime()).nextInt(
					backgroundImage.getWidth(this)
					- targetImage.getWidth(this)) + topX;
			targetY = new Random(System.nanoTime()).nextInt(
					backgroundImage.getHeight(this)
					- targetImage.getHeight(this)) + topY;
			
			backgroundArea = new Rectangle2D.Double(topX, topY, 
					backgroundImage.getWidth(this),
					backgroundImage.getHeight(this));
			targetArea = new Rectangle2D.Double(targetX, targetY,
					targetImage.getWidth(this),
					targetImage.getHeight(this));
		} catch(IllegalArgumentException e) {
			System.err.println("ERROR: target image is bigger than background image!");
			
			ExperimentData data = new ExperimentData();
			data.setBackgroundName(backgroundName);
			data.setTargetName(targetName);
			data.setFound(false);
			data.setPassed(false);
			data.setTime(-891020);
			dispatcher.addExperimentData(data);
			
			endExperiment();
			return;
		} catch(Exception e) {
			e.printStackTrace();
		}

		this.repaint();
	}
	
	private Image loadImage(File imageFile) {
		
		if(null == imageFile)
			return null;
		
		Image loadedImage = null;
		try {
			loadedImage = ImageIO.read(imageFile);
		} catch(IOException e) {
			System.err.println("ERROR: target image is missing!");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return loadedImage;
	}
	
	private void endExperiment() {

		showTimer = new Timer(0, nullActionListener);
		breakTimer = new Timer(0, nullActionListener);

		dispatcher.endNotify();
	}
}
