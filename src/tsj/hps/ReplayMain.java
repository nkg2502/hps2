package tsj.hps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.*;

import tsj.hps.model.DataManager;
import tsj.hps.view.ViewManager;

public class ReplayMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// read predefined values
		String predefinedShowTimeInterval = null;
		String predefinedBreakTimeInterval = null;
		
		// FIXME: refactoring
		try {
			JSONObject predefinedSetting = (JSONObject) JSONValue.parse((new FileReader(new File(DataManager.SETTING_FILE))));
			predefinedShowTimeInterval = predefinedSetting.get("showTimeInterval").toString();
			predefinedBreakTimeInterval = predefinedSetting.get("breakTimeInterval").toString();
			
		} catch(FileNotFoundException e) {
			;
		} catch(NullPointerException e) {
			;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ViewManager.getInstance().init();
		ViewManager.getInstance().replyDialog(predefinedShowTimeInterval, predefinedBreakTimeInterval);
	}

}
