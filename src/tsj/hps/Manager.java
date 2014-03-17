package tsj.hps;

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
public class Manager implements Observer {
	
	public final static String SETTING_FILE = "setting.json";
	public final static boolean FEMALE = true;
	public final static boolean MALE = false;
	
	private final static Manager instance = new Manager();
	
	private Dispatcher dispatcher = null;
	
	private int age = 0;
	private boolean gender = FEMALE;
	private int showTimeInterval = 0;
	private int breakTimeInterval = 0;
	private File backgroundPath = null;
	private File targetPath = null; 
	
	private Manager() {}
	
	public static Manager getInstance() {
		return instance;
	}
	
	public Dispatcher getDispatcher() {
		this.dispatcher = new Dispatcher(backgroundPath, targetPath);
		this.dispatcher.addObserver(this);
		
		return this.dispatcher;
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
			reportWriter.print(STATUS(i.isFound(), i.isPassed(), i.getTime(), showTimeInterval));
			reportWriter.println();
		}
		
		reportWriter.close();
		
	}
	
	private static String GENDER(boolean type) {
		return (FEMALE == type ? "Female" : "Male");
	}
	
	private static String STATUS(boolean isFound, boolean isPassed, long time, long maxTime) {
		
		if(0 > time) 
			return "ERROR";
		else if(time >= maxTime)
			return "Timeout";
		else if(isFound)
			return "Found";
		else if(isPassed)
			return "Passed";
		else
			return "Miss Clicked";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		
		writeReport((List<ExperimentData>) arg);
		System.exit(0);
	}
	
	// FOR DEBUGGING
	public static void PRINT_ALL(Iterable<?> list, String msg) {
		for(Object i: list) {
			System.out.println("[" + msg + "]: " + i);
		}
		System.out.println();
	}
}
