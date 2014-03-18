package tsj.hps;

import java.io.File;

public class PrettyNamedFile extends File {

	/**
	 * Secret signature
	 */
	private static final long serialVersionUID = 19860728163000L;

	public PrettyNamedFile(File parent, String child) {
		super(parent, child);
	}
	
	@Override
	public String toString() {
		return super.getName();
	}
	

}
