package tsj.hps.model;

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

import tsj.hps.ds.ExperimentData;

/**
 * manage all information
 * 
 * @author Taesung Jung 
 *
 */
public class DataManager implements Observer {
	
	public final static String SETTING_FILE = "setting.json";
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
	

	
	public void writeReport(List<ExperimentData> resultList) {
		
		GregorianCalendar now = new GregorianCalendar();
		String reportFileName = String.format("%4d.%02d.%02d[%02d.%02d.%02d].%s.%02d.csv",
				now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1,
				now.get(Calendar.DATE),
				now.get(Calendar.HOUR_OF_DAY),
				now.get(Calendar.MINUTE),
				now.get(Calendar.SECOND),
				GENDER(gender), age);
		
		PrintWriter reportWriter = null;
		
		// check result folder
		File resultFolder = new File("result");
		if(!resultFolder.isDirectory()) 
			if(!resultFolder.mkdir()) {
				System.err.println("ERROR: MAKE result FOLDER!");
				resultFolder = null;
			}
		
		try {
			reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(resultFolder, reportFileName))));
		} catch(IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
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
		
		// TODO: summarize Report
		
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
			reportWriter.print(i.getBackgroundName());
			reportWriter.print(",");
			reportWriter.print(i.getTargetName());
			reportWriter.print(",");
			reportWriter.print(i.getTime());
			reportWriter.print(",");
			reportWriter.print(STATUS_2_STRING(STATUS(i.isFound(), i.isPassed(), i.getTime(), showTimeInterval)));
			reportWriter.println();
		}
		
		reportWriter.close();
		
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
	
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		
		writeReport((List<ExperimentData>) arg);
	}
	
	// FOR DEBUGGING
	public static void PRINT_ALL(Iterable<?> list, String msg) {
		for(Object i: list) {
			System.out.println("[" + msg + "]: " + i);
		}
		System.out.println();
	}
}
