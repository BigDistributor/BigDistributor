package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.clustering.kafka.JobConsumer;
import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class StatusListenerManager implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		callback.log("Get Status..");
		JobConsumer consumerThread = new JobConsumer();
		consumerThread.start();
	}

}
