package tsj.hps;

import java.io.File;

/**
 * manage every information
 * 
 * @author Taesung Jung 
 *
 */
public class Manager {
	
	public final static boolean FEMALE = true;
	public final static boolean MALE = false;
	
	private final static Manager instance = new Manager();
	
	private int age;
	private boolean gender;
	private int showTimeInterval;
	private int breakTimeInterval;
	private File backgroundPath;
	private File targetPath;
	
	private Manager() {}
	
	public static Manager getInstance() {
		return instance;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public boolean isGender()
	{
		return gender;
	}

	public void setGender(boolean gender)
	{
		this.gender = gender;
	}

	public int getShowTimeInterval()
	{
		return showTimeInterval;
	}

	public void setShowTimeInterval(int showTimeInterval)
	{
		this.showTimeInterval = showTimeInterval;
	}

	public int getBreakTimeInterval()
	{
		return breakTimeInterval;
	}

	public void setBreakTimeInterval(int breakTimeInterval)
	{
		this.breakTimeInterval = breakTimeInterval;
	}

	public File getBackgroundPath()
	{
		return backgroundPath;
	}

	public void setBackgroundPath(File backgroundPath)
	{
		this.backgroundPath = backgroundPath;
	}

	public File getTargetPath()
	{
		return targetPath;
	}

	public void setTargetPath(File targetPath)
	{
		this.targetPath = targetPath;
	}
	
	@Override
	public String toString() {
		return "";
		
	}
	
	

}
