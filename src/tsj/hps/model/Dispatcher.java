package tsj.hps.model;

import java.util.Observable;

import tsj.hps.ds.ExperimentData;
import tsj.hps.ds.ImageNode;

/**
 * 
 * @author Taesung Jung 
 *
 */
public abstract class Dispatcher extends Observable {

	public abstract ImageNode popImage(); 
	public abstract void addExperimentData(ExperimentData data);
	public abstract void endNotify();
}
