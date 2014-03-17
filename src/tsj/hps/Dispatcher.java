package tsj.hps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
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
public class Dispatcher extends Observable {
	
	/**
	 * Shuffled background image and target image List
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
		
		// shuffle background images and target images
		List<File> shuffledBackgroundImageList = shuffle(backgroundImageList, targetImageList.size(), targetImageList.size());
		List<File> shuffledTargetImageList = shuffle(targetImageList, backgroundImageList.size(), 0);
		
		int imageListSize = shuffledBackgroundImageList.size();
		for(int i = 0; i < imageListSize; ++i)
			shuffledImageList.add(new ImageNode(shuffledBackgroundImageList.get(i),
					shuffledTargetImageList.get(i)));
	}
	
	private static List<File> shuffle(List<File> list, int repeat, int interval) {
		
		List<File> shuffledList = new ArrayList<File>();
		
		// randomize algorithm
		List<ShuffleNode> shuffleNodeList = new ArrayList<ShuffleNode>();
		for(File image: list) 
			shuffleNodeList.add(new ShuffleNode(image, repeat - 1));
		
		Queue<ShuffleNode> excludedQueue = new LinkedList<ShuffleNode>();
		List<ShuffleNode> candidateList = new ArrayList<ShuffleNode>();
		
		Collections.shuffle(shuffleNodeList, 
				new Random(System.nanoTime()));
		
		// initial list
		for(ShuffleNode i: shuffleNodeList)
			shuffledList.add(i.getImage());
		
		while(0 < shuffleNodeList.size() - interval)
			candidateList.add(shuffleNodeList.remove(0));
		
		while(0 < shuffleNodeList.size())
			excludedQueue.add(shuffleNodeList.remove(0));
		
		while(0 < candidateList.size() || 0 < excludedQueue.size()) {
			Collections.shuffle(candidateList, 
					new Random(System.nanoTime()));
			
			ShuffleNode popItem = null;
			
			if(0 < candidateList.size())
				popItem = candidateList.remove(0);
			
			if(null != popItem) {
				
				shuffledList.add(popItem.getImage());
				
				popItem.used();
				if(popItem.canRepeat())
					excludedQueue.add(popItem);
			}
			
			if(0 < excludedQueue.size())
				candidateList.add(excludedQueue.remove());
		}
		
		return shuffledList;
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
	
	public void endNotify() {
		this.setChanged();
		this.notifyObservers(experimentDataList);
	}
}
