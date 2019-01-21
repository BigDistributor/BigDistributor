package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.clustering.kafka.JobConsumer;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class StatusListenerManager implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		callback.log("Get Status..");
		JobConsumer consumerThread = new JobConsumer(callback);
		consumerThread.start();
	}

}
