package tsj.hps.ds;

import java.awt.Point;
import java.io.File;

/**
 * Experiment Result
 * 
 * @author Taesung Jung
 *
 */
public class ExperimentData {
	
	/**
	 * Background image name.
	 */
	private File backgroundPath;
	
	/**
	 * Target image name.
	 */
	private File targetPath;
	
	/**
	 * Found time.
	 */
	private long time;
	
	/**
	 * 
	 */
	private boolean isFound = false;
	
	/**
	 * 
	 */
	private boolean isPassed = false;
	
	/**
	 * Target's left top point.
	 * 
	 */
	private Point targetPoint = new Point(0, 0);
	
	public void setBackgroundPath(File path) {
		this.backgroundPath = path;
	}
	
	public void setTargetPath(File path) {
		this.targetPath = path;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}

	public File getBackgroundPath() {
		return backgroundPath;
	}

	public File getTargetPath() {
		return targetPath;
	}

	public long getTime() {
		return time;
	}

	public boolean isFound() {
		return isFound;
	}

	public boolean isPassed() {
		return isPassed;
	}
	
	public Point getTargetPoint() {
		return targetPoint;
	}

	public void setTargetPoint(int x, int y) {
		this.targetPoint = new Point(x, y);
	}

	@Override
	public String toString()
	{
		return backgroundPath.getAbsolutePath()
			+ ": " + targetPath.getAbsolutePath()
			+ ": " + time
			+ ":[F] " + isFound
			+ ":[P] " + isPassed
			+ ":(" + targetPoint.x + ", " + targetPoint.y + ")"
			;
	}
}
