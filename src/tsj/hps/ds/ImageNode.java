package tsj.hps.ds;

import java.io.File;

/**
 * 
 * @author Taesung Jung
 *
 */
public class ImageNode {
	
	private File backgroundImage = null;
	private File targetImage = null;
	
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
	
	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setTargetImage(File targetImage) {
		this.targetImage = targetImage;
	}
	
	@Override
	public String toString() {
		return this.backgroundImage.getName() + ":" + this.targetImage.getName();
	}

}
