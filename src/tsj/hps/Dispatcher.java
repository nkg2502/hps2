package tsj.hps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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
	*/
	
	private List<File> backgroundImageList = null;
	
	private List<File> targetImageList = null;
	
	/**
	 * Experiment Result data
	 */
	private List<ExperimentData> experimentDataList = null;
	
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
		
		this.experimentDataList = new ArrayList<ExperimentData>();
		
		// TODO: add ramdomize algorithm
		
		// Initialize backgroundImageList
		backgroundImageList = new ArrayList<File>();
		for(File i: backgroundPath.listFiles(new ExtensionFilter())) 
			backgroundImageList.add(i);
		
		if(0 >= backgroundImageList.size())
			throw new IllegalArgumentException("Background directory is empty!");
		
		// Initialize targetImageList
		targetImageList = new ArrayList<File>();
		for(File i: targetPath.listFiles(new ExtensionFilter())) 
			targetImageList.add(i);
		
		if(0 >= targetImageList.size())
			throw new IllegalArgumentException("Target directory is empty!");
	
	}
	
	public File popBackgroundImage() {
		if(0 < backgroundImageList.size())
			return backgroundImageList.remove(0);
		return null;
	}
	
	public File popTargetImage() {
		if(0 < targetImageList.size())
			return targetImageList.remove(0);
		return null;
	}
	
	public List<ExperimentData> getExperimentResult() {
		return experimentDataList;
	}

	public void addExperimentData(ExperimentData data) {
		experimentDataList.add(data);
	}
	
}
