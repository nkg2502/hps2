package tsj.hps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import tsj.hps.model.DataManager;
import tsj.hps.view.ViewManager;

public class Main {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args)
	{
		// read predefined values
		String predefinedShowTimeInterval = null;
		String predefinedBreakTimeInterval = null;
		
		try {
			JSONObject predefinedSetting = (JSONObject) JSONValue.parse((new FileReader(new File(DataManager.SETTING_FILE))));
			predefinedShowTimeInterval = predefinedSetting.get("showTimeInterval").toString();
			predefinedBreakTimeInterval = predefinedSetting.get("breakTimeInterval").toString();
		} catch(FileNotFoundException e) {
			;
		} catch(Exception e) {
			e.printStackTrace();
		}

		// TODO: modify UI
		ViewManager viewManager = ViewManager.getInstance();
		viewManager.init();
		viewManager.experimentDialog(predefinedShowTimeInterval, predefinedBreakTimeInterval);
	}
}