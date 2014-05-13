package tsj.hps.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import tsj.hps.ds.ExperimentData;

/**
 * manage all information
 * 
 * @author Taesung Jung 
 *
 */
public class DataManager implements Observer {
	
	public final static String SETTING_FILE = "setting.json";
	public final static String RESULT_FOLDER = "result";
	public final static boolean FEMALE = true;
	public final static boolean MALE = false;
	
	public final static int USER_FOUND = 0;
	public final static int USER_PASSED = 1;
	public final static int USER_MISS_CLICKED = 2;
	public final static int USER_TIMEOUT = 3;
	public final static int USER_ERROR = 4;
	private final static int USER_STATUS_SUMMARY_SIZE = 5;
	
	public final static int TIME_TOTAL = 0;
	public final static int TIME_USED = 1;
	private final static int TIME_SUMMARY_SIZE = 2;
	
	private final static DataManager instance = new DataManager();
	
	private int age = 0;
	private boolean gender = FEMALE;
	private int showTimeInterval = 0;
	private int breakTimeInterval = 0;
	private File backgroundPath = null;
	private File targetPath = null; 
	
	private DataManager() {}
	
	public static DataManager getInstance() {
		return instance;
	}
	
	public static BufferedImage loadImage(File imageFile) {
		
		if(null == imageFile)
			return null;
		
		BufferedImage loadedImage = null;
		try {
			loadedImage = ImageIO.read(imageFile);
		} catch(IOException e) {
			System.err.println("ERROR: image is missing!");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return loadedImage;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public int getShowTimeInterval() {
		return showTimeInterval;
	}

	public void setShowTimeInterval(int showTimeInterval) {
		this.showTimeInterval = showTimeInterval;
	}

	public int getBreakTimeInterval() {
		return breakTimeInterval;
	}

	public void setBreakTimeInterval(int breakTimeInterval) {
		this.breakTimeInterval = breakTimeInterval;
	}

	public File getBackgroundPath() {
		return backgroundPath;
	}

	public void setBackgroundPath(File backgroundPath) {
		this.backgroundPath = backgroundPath;
	}

	public File getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(File targetPath) {
		this.targetPath = targetPath;
	}
	
	@SuppressWarnings("unchecked")
	public void writeReport(List<ExperimentData> resultList, File reportFolder, GregorianCalendar now) {
		
		String reportFileName = GENDER(gender) + "." + Integer.toString(age);
		PrintWriter reportWriter = null;
	
		try {
			reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(reportFolder, reportFileName + ".csv"))));
		} catch(IOException e) {
			System.err.println("ERROR: MAKE report!");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		reportWriter.println("Date, " + String.format("%4d.%02d.%02d (%02d:%02d:%02d)", 
				now.get(Calendar.YEAR), 
				now.get(Calendar.MONTH) + 1, 
				now.get(Calendar.DATE), 
				now.get(Calendar.HOUR_OF_DAY), 
				now.get(Calendar.MINUTE), 
				now.get(Calendar.SECOND))); 
		reportWriter.println("Gender, " + GENDER(gender));
		reportWriter.println("Age, " + age);
		reportWriter.println("Show Time Interval, " + showTimeInterval);
		reportWriter.println("Break Time Interval, " + breakTimeInterval);
		reportWriter.println("Background Folder, " + backgroundPath.getAbsolutePath());
		reportWriter.println("Target Folder, " + targetPath.getAbsolutePath());
		
		reportWriter.println();
		reportWriter.println("Date, Gender, Age, Back Image, Target Image, Time, Status");
		
		String today = String.format("%02d/%02d/%04d", now.get(Calendar.MONTH) + 1, now.get(Calendar.DATE), now.get(Calendar.YEAR));
		for(ExperimentData i: resultList) {
			
			reportWriter.print(today);
			reportWriter.print(",");
			reportWriter.print(GENDER(gender));
			reportWriter.print(",");
			reportWriter.print(age);
			reportWriter.print(",");
			reportWriter.print(i.getBackgroundPath().getName());
			reportWriter.print(",");
			reportWriter.print(i.getTargetPath().getName());
			reportWriter.print(",");
			reportWriter.print(i.getTime());
			reportWriter.print(",");
			reportWriter.print(STATUS_2_STRING(STATUS(i.isFound(), i.isPassed(), i.getTime(), showTimeInterval)));
			reportWriter.println();
		}
		
		reportWriter.close();
		
		// write replay log
		PrintWriter logWriter = null;
		try {
			logWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(reportFolder, reportFileName + ".replay"))));
		} catch(IOException e) {
			System.err.println("ERROR: MAKE replay log!");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JSONObject replayObject = new JSONObject();
		replayObject.put("showTimeInterval", showTimeInterval);
		replayObject.put("breakTimeInterval", breakTimeInterval);
		
		JSONArray replaySequence = new JSONArray();
		for(ExperimentData i: resultList) {
			JSONObject item = new JSONObject();

			item.put("backgroundPath", i.getBackgroundPath().getAbsolutePath());
			item.put("targetPath", i.getTargetPath().getAbsolutePath());
			item.put("targetX", i.getTargetPoint().x);
			item.put("targetY", i.getTargetPoint().y);
			item.put("status", (STATUS_2_STRING(STATUS(i.isFound(), i.isPassed(), i.getTime(), showTimeInterval))));
			replaySequence.add(item);
		}
		
		replayObject.put("replay", replaySequence);
		
		logWriter.println(replayObject);
		logWriter.close();
	}
	
	public void takeScreenshot(List<ExperimentData> resultList, File reportFolder) {
		
		int sceneNumber = 0;

		for(ExperimentData i: resultList) {
			BufferedImage backgroundImage = loadImage(i.getBackgroundPath());
			BufferedImage targetImage = loadImage(i.getTargetPath());
			
			BufferedImage screenshot = new BufferedImage(backgroundImage.getWidth(),
					backgroundImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graphics = (Graphics2D) screenshot.getGraphics();
			
			graphics.setBackground(Color.BLACK);
			graphics.drawImage(backgroundImage, 0, 0, null);
			graphics.drawImage(targetImage, i.getTargetPoint().x, i.getTargetPoint().y, null);
			
			String screenshotName = String.format("%03d.png", ++sceneNumber);
			try {
				ImageIO.write(screenshot, "png", new File(reportFolder, screenshotName));
			} catch(IOException e) {
				System.err.println("ERROR: MAKE screenshot!");
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {

		// check result folder
		File resultFolder = new File(RESULT_FOLDER);
		if(!resultFolder.isDirectory()) {
			if(!resultFolder.mkdir()) {
				System.err.println("ERROR: MAKE result FOLDER!");
				resultFolder = null;
			}
		}
		
		GregorianCalendar now = new GregorianCalendar();
		String reportFolderName = String.format("%4d.%02d.%02d[%02d.%02d.%02d].%s.%02d",
				now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1,
				now.get(Calendar.DATE),
				now.get(Calendar.HOUR_OF_DAY),
				now.get(Calendar.MINUTE),
				now.get(Calendar.SECOND),
				GENDER(gender), age);

		// check report folder
		File reportFolder = new File(resultFolder, reportFolderName);
		if(!reportFolder.isDirectory()) {
			if(!reportFolder.mkdir()) {
				System.err.println("ERROR: MAKE report FOLDER!");
				reportFolder = null;
			}
		}
		
		writeReport((List<ExperimentData>) arg, reportFolder, now);
		takeScreenshot((List<ExperimentData>) arg, reportFolder);
	
	}

	/**
	 * Summarize status log.
	 * 
	 * @param resultList
	 * 	Experiment data.
	 * @return counter
	 * 	Each status value.
	 */
	public static int[] summarizeStatus(List<ExperimentData> resultList) {
		
		int[] summary = new int[USER_STATUS_SUMMARY_SIZE];
		
		for(int i = 0; i < USER_STATUS_SUMMARY_SIZE; ++i)
			summary[i] = 0;
		
		long maxTime = DataManager.getInstance().getShowTimeInterval();
		for(ExperimentData i: resultList) {
			++summary[STATUS(i.isFound(), i.isPassed(), i.getTime(), maxTime)];
		}
		
		return summary;
	}
	
	/**
	 * Summarize time log.
	 * 
	 * @param resultList
	 * @return
	 */
	public static long[] summarizeTime(List<ExperimentData> resultList) {
		
		long[] summary = new long[TIME_SUMMARY_SIZE];
		
		for(int i = 0; i < TIME_SUMMARY_SIZE; ++i) 
			summary[i] = 0;
		
		summary[TIME_TOTAL] = DataManager.getInstance().getShowTimeInterval() * resultList.size();
			
		for(ExperimentData i: resultList)
			summary[TIME_USED] += i.getTime();
		
		return summary;
	}

	
	private static String GENDER(boolean type) {
		return (FEMALE == type ? "Female" : "Male");
	}
	
	private static int STATUS(boolean isFound, boolean isPassed, long time, long maxTime) {
		
		if(0 > time)
			return USER_ERROR;
		else if(time >= maxTime)
			return USER_TIMEOUT;
		else if(isFound)
			return USER_FOUND;
		else if(isPassed)
			return USER_PASSED;
		else
			return USER_MISS_CLICKED;
	}
	
	private static String STATUS_2_STRING(int status) {
		
		switch(status) {
			case USER_FOUND:
				return "Found";
			case USER_PASSED:
				return "Passed";
			case USER_MISS_CLICKED:
				return "Miss Clicked";
			case USER_TIMEOUT:
				return "Timeout";
			default:
				return "Error";
		}
	}
	
	// FOR DEBUGGING
	public static void PRINT_ALL(Iterable<?> list, String msg) {
		for(Object i: list) {
			System.out.println("[" + msg + "]: " + i);
		}
		System.out.println();
	}
}
