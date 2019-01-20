package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.clustering.kafka.JobConsumer;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import mpicbg.spim.io.IOFunctions;

public class StatusListenerManager {
	public static void startStatusListener() {
		IOFunctions.println("Get Status..");
		JobConsumer consumerThread = new JobConsumer(new AbstractCallBack() {
			
			@Override
			public void onSuccess(int pos) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void log(String log) {
				// TODO Auto-generated method stub
				
			}
		});
		consumerThread.start();
	}

}
