package tsj.hps.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.*;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.ImageNode;
import tsj.hps.ds.ReplayImageNode;

public class ReplayDispatcher extends Dispatcher {
	
	/**
	 * Replay image List.
	 */
	private List<ReplayImageNode> replayList = new ArrayList<ReplayImageNode>();

	// FIXME: refactoring
	public ReplayDispatcher(String replayPath) {
		
		try {
			JSONObject replayObject = (JSONObject) JSONValue.parse((new FileReader(new File(replayPath))));
			JSONArray replayArray = (JSONArray) replayObject.get("replay");
			
			for(int i = 0; i < replayArray.size(); ++i) {
				JSONObject replayItem = (JSONObject) replayArray.get(i);
				
				File backgroundImage = new File(replayItem.get("backgroundPath").toString());
				File targetImage = new File(replayItem.get("targetPath").toString());
	
				int targetX = Integer.parseInt(replayItem.get("targetX").toString());
				int targetY = Integer.parseInt(replayItem.get("targetY").toString());
				
				if(!backgroundImage.exists() || !targetImage.exists())
					throw new NullPointerException();
			
				replayList.add(new ReplayImageNode(backgroundImage, targetImage, targetX, targetY));
			}
			
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(NumberFormatException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public ImageNode popImage() {
		if(0 < replayList.size())
			return replayList.remove(0);
		return null;
	}

	@Override
	public void addExperimentData(ExperimentData data) {}

	@Override
	public void endNotify() {
		System.exit(0);
	}

}
