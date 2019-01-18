package main.java.com.controllers.tasks;

import main.java.com.clustering.scripting.BatchGenerator;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.AppMode;
import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class TaskBatchGenerator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		IOFunctions.println("Generate Batch..");
		if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
			BatchGenerator.GenerateBatchForLocalFiles(Config.parallelJobs, Config.getTotalInputFiles(), callback, pos);
		} else if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
			BatchGenerator.GenerateBatchForClusterFile(callback, pos);
		}
	
		
	}

}
