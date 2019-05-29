package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class TaskBatchGenerator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		callback.log("Generate Batch..");
		
//			BatchGenerator.GenerateBatchForClusterFile(callback, Job.getTotalBlocks(), pos);
		}
	

}
