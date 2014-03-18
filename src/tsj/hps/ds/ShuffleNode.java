package tsj.hps.ds;

import java.io.File;

/**
 * Uses background image shuffle algorithm.
 * 
 * @author Taesung Jung
 *
 */
public class ShuffleNode {
	
	private File image = null;
	private int repeat = 0;
	
	public ShuffleNode(File image, int repeat) {
		this.image = image;
		this.repeat = repeat; 
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
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
		return this.image.getName() + ":" + repeat;
	}
}
