package tsj.hps.ds;

import java.io.File;
import java.util.Random;

public class RandomImageNode extends ImageNode {

	public RandomImageNode(File backgroundImage, File targetImage) {
		super(backgroundImage, targetImage);
	}
	
	@Override
	public int getTargetX(int max) {
		return new Random(System.nanoTime()).nextInt(max);
	}
	
	@Override
	public int getTargetY(int max) {
		return new Random(System.nanoTime()).nextInt(max);
	}

}
