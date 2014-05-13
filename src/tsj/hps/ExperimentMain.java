package tsj.hps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import tsj.hps.controller.DataManager;
import tsj.hps.controller.ViewManager;

public class ExperimentMain {

	/**
	 * @param args
	 * 	-replay : replay mode
	 * 
	 */
	public static void main(String[] args) {
		
		boolean isReplayMode = false;
		for(String i: args) {
			isReplayMode = i.equalsIgnoreCase("-replay");
		}
		
		// read predefined values
		String predefinedShowTimeInterval = null;
		String predefinedBreakTimeInterval = null;
		String predefinedAge = null;
		
		try {
			JSONObject predefinedSetting = (JSONObject) JSONValue.parse((new FileReader(new File(DataManager.SETTING_FILE))));
			predefinedShowTimeInterval = predefinedSetting.get("showTimeInterval").toString();
			predefinedBreakTimeInterval = predefinedSetting.get("breakTimeInterval").toString();
			predefinedAge = predefinedSetting.get("age").toString();
			
		} catch(FileNotFoundException e) {
			;
		} catch(NullPointerException e) {
			;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ViewManager viewManager = ViewManager.getInstance();
		viewManager.init();

		if(isReplayMode)
			viewManager.replyDialog(predefinedShowTimeInterval, predefinedBreakTimeInterval);
		else
			viewManager.experimentDialog(predefinedShowTimeInterval, predefinedBreakTimeInterval, predefinedAge);
	}
}