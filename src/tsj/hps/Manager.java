package tsj.hps;

import java.io.File;
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
	
	@Override
	public String toString() {
		return "";
	}

	@Override
	public void update(Observable o, Object arg) {
		
		List<ExperimentData> list = (List<ExperimentData>) arg;
		
		PRINT_ALL(list, "RESULT");
		
	}
	
	public static void PRINT_ALL(Iterable<?> list, String msg) {
		for(Object i: list) {
			System.out.println("[" + msg + "]: " + i);
		}
		System.out.println();
	}
}
