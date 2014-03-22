package tsj.hps.ds;

import java.io.File;

/**
 * 
 * @author Taesung Jung
 *
 */
public abstract class ImageNode {
	
	private File backgroundImage = null;
	private File targetImage = null;
	
	private int targetX = 0;
	private int targetY = 0;
	
	public abstract int getTargetX(int max);
	public abstract int getTargetY(int max);
	
	public ImageNode(File backgroundImage, File targetImage) {
		this.backgroundImage = backgroundImage;
		this.targetImage = targetImage;
	}
	
	public File getBackgroundImage() {
		return backgroundImage;
	}
	
	public File getTargetImage() {
		return targetImage;
	}
	
	public int getTargetX() {
		return targetX;
	}
	
	public int getTargetY() {
		return targetY;
	}
	
	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setTargetImage(File targetImage) {
		this.targetImage = targetImage;
	}
	
	public void setTargetX(int x) {
		this.targetX = x;
	}

	public void setTargetY(int y) {
		this.targetY = y;
	}

	@Override
	public String toString() {
		return this.backgroundImage.getName() + ":" + this.targetImage.getName();
	}
}
