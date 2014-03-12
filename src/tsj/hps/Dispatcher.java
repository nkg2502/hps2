package tsj.hps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.ImageNode;
import tsj.hps.ds.ShuffleNode;

/**
 * Image list dispatcher
 * 
 * @author Taesung Jung 
 *
 */
public class Dispatcher {
	
	/* maybe no need
	private File backgroundPath = null;
	private File targetPath = null;
	
	private List<File> backgroundImageList = null;
	
	private List<File> targetImageList = null;
	
	*/
	
	private List<ImageNode> shuffledImageList = new ArrayList<ImageNode>();

	/**
	 * Experiment Result data
	 */
	private List<ExperimentData> experimentDataList = new ArrayList<ExperimentData>();
	
	private class ExtensionFilter implements FilenameFilter {
		
		@Override
		public boolean accept(File dir, String name)
		{
			name = name.toLowerCase();
			return name.endsWith(".jpg")
				|| name.endsWith(".gif")
				|| name.endsWith(".png")
				|| name.endsWith(".jpeg");
		}
	}
	
	public Dispatcher(File backgroundPath, File targetPath) {
		
		// Initialize backgroundImageList
		List<File> backgroundImageList = new ArrayList<File>();
		for(File i: backgroundPath.listFiles(new ExtensionFilter())) 
			backgroundImageList.add(i);
		
		if(0 >= backgroundImageList.size())
			throw new IllegalArgumentException("Background directory is empty!");
		
		// Initialize targetImageList
		List<File> targetImageList = new ArrayList<File>();
		for(File i: targetPath.listFiles(new ExtensionFilter())) 
			targetImageList.add(i);
		
		if(0 >= targetImageList.size())
			throw new IllegalArgumentException("Target directory is empty!");
		
		// Initialize backgroundImageList and targetImageList
		
		// TODO: add ramdomize algorithm
		List<ShuffleNode> rawImageList = new ArrayList<ShuffleNode>();
		int repeatSize = targetImageList.size();
		for(File backgroundImage: backgroundImageList) 
			rawImageList.add(new ShuffleNode(backgroundImage, repeatSize - 1));
		
		// randomize algorithm
		// TODO: 타겟도 잘 섞어야 할 거 같다. 
		Queue<ShuffleNode> excludedQueue = new LinkedList<ShuffleNode>();
		List<ShuffleNode> candidateList = new ArrayList<ShuffleNode>();
		
		Collections.shuffle(rawImageList, 
				new Random(System.nanoTime()));
		
		for(ShuffleNode i: rawImageList)
			shuffledImageList.add(new ImageNode(i.getBackgroundImage()));
		
		for(ShuffleNode i: rawImageList) {
			System.out.println("raw" + i);
		}
		System.out.println();
		
		while(0 < rawImageList.size() - repeatSize)
			candidateList.add(rawImageList.remove(0));
		
		while(0 < rawImageList.size())
			excludedQueue.add(rawImageList.remove(0));
		
		for(ShuffleNode i: candidateList) {
			System.out.println("candi " + i);
		}
		System.out.println();
	
		for(ShuffleNode i: excludedQueue) {
			System.out.println("ex " + i);
		}
		System.out.println();
		
		while(0 < candidateList.size() || 0 < excludedQueue.size()) {
			Collections.shuffle(candidateList, 
					new Random(System.nanoTime()));
			
			ShuffleNode popItem = null;
			
			if(0 < candidateList.size())
				popItem = candidateList.remove(0);
			
			if(null != popItem) {
				
				shuffledImageList.add(new ImageNode(popItem.getBackgroundImage()));
				
				popItem.used();
				if(popItem.canRepeat())
					excludedQueue.add(popItem);
			}
			
			if(0 < excludedQueue.size())
				candidateList.add(excludedQueue.remove());
		}
		
		Collections.shuffle(targetImageList, 
				new Random(System.nanoTime()));
		
		// TODO: 여기 개선
		int targetIndex = 0;
		for(ImageNode i: shuffledImageList) {
			i.setTargetImage(targetImageList.get(targetIndex));
			
			++targetIndex;
			if(targetIndex >= targetImageList.size())
				targetIndex = 0;
		}
		
		
		for(ImageNode i: shuffledImageList) {
			System.out.println("last " + i);
		}
		System.out.println();
	
		
	}
	
	@Deprecated
	public File popBackgroundImage() {
		return null;
	}
	
	@Deprecated
	public File popTargetImage() {
		return null;
	}
	
	public ImageNode popImage() {
		if(0 < shuffledImageList.size())
			return shuffledImageList.remove(0);
		return null;
	}
	
	public List<ExperimentData> getExperimentResult() {
		return experimentDataList;
	}

	public void addExperimentData(ExperimentData data) {
		experimentDataList.add(data);
	}
	
}
