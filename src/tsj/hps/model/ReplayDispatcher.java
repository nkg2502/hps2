package tsj.hps.model;

import java.io.File;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.ImageNode;

public class ReplayDispatcher extends Dispatcher {

	public ReplayDispatcher(File replayFile) {
		
	}
	
	@Override
	public ImageNode popImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExperimentData(ExperimentData data) { }

	@Override
	public void endNotify() {
		System.exit(0);
	}

}
