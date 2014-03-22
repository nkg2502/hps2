package tsj.hps.ds;

import java.awt.Point;

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
	private String backgroundName;
	
	/**
	 * Target image name.
	 */
	private String targetName;
	
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
	
	private Point targetPoint = new Point(0, 0);
	
	public void setBackgroundName(String name) {
		this.backgroundName = name;
	}
	
	public void setTargetName(String name) {
		this.targetName = name;
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

	public String getBackgroundName() {
		return backgroundName;
	}

	public String getTargetName() {
		return targetName;
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
		return backgroundName
			+ ": " + targetName
			+ ": " + time
			+ ":[F] " + isFound
			+ ":[P] " + isPassed
			;
	}
}
