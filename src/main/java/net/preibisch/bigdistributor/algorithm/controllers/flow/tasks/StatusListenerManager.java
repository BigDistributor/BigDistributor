package net.preibisch.bigdistributor.algorithm.controllers.flow.tasks;

import net.preibisch.bigdistributor.algorithm.errorhandler.callback.AbstractCallBack;
import net.preibisch.bigdistributor.algorithm.clustering.kafka.JobConsumer;
import net.preibisch.bigdistributor.algorithm.controllers.flow.AbstractTask;

public class StatusListenerManager implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		callback.log("Get Status..");
		JobConsumer consumerThread = new JobConsumer();
		consumerThread.start();
	}

}
