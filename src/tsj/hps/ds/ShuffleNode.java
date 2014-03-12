package tsj.hps.ds;

import java.io.File;

public class ShuffleNode {
	
	private File backgroundImage = null;
	private int repeat = 0;
	
	public ShuffleNode(File backgroundImage, int repeat) {
		this.backgroundImage = backgroundImage;
		this.repeat = repeat; 
	}

	public File getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public boolean canRepeat() {
		return 0 < this.repeat ? true : false;
	}
	
	public void used() {
		--this.repeat;
	}
	
	@Override
	public String toString() {
		return this.backgroundImage.getName() + ":" + repeat;
	}
}
