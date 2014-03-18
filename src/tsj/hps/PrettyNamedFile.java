package tsj.hps;

import java.io.File;

public class PrettyNamedFile extends File {

	/**
	 * 
	 */
	// FIXME: serial number
	private static final long serialVersionUID = -2288242849733330611L;

	public PrettyNamedFile(File parent, String child) {
		super(parent, child);
	}
	
	@Override
	public String toString() {
		return super.getName();
	}
	

}
