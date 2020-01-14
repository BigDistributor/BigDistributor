package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class TaskBatchGenerator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		callback.log("Generate Batch..");
		
//			BatchGenerator.GenerateBatchForClusterFile(callback, Job.getTotalBlocks(), pos);
		}
	

}
