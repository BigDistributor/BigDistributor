package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.clustering.scripting.BatchGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;

public class TaskBatchGenerator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		callback.log("Generate Batch..");
		if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
			BatchGenerator.GenerateBatchForLocalFiles(Config.parallelJobs, Config.getTotalInputFiles(), callback, pos);
		} else if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
			BatchGenerator.GenerateBatchForClusterFile(callback, Config.getTotalInputFiles(), pos);
		}
	
		
	}

}
