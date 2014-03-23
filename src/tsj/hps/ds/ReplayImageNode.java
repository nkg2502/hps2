package tsj.hps.ds;

import java.io.File;

public class ReplayImageNode extends ImageNode {

	public ReplayImageNode(File backgroundImage, File targetImage) {
		super(backgroundImage, targetImage);
	}
	
	public ReplayImageNode(File backgroundImage, File targetImage, int targetX, int targetY, String extra) {
		super(backgroundImage, targetImage);
		setTargetX(targetX);
		setTargetY(targetY);
		setExtra(extra);
	}
	
	@Override
	public int getTargetX(int max) { 
		return super.getTargetX();
	}

	@Override
	public int getTargetY(int max) {
		return super.getTargetY();
	}

}
