package main.java.com.clustering.workflow;

import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.kafka.JobConsumer;
import mpicbg.spim.io.IOFunctions;

public class StatusListenerManager {
	public static void startStatusListener() {
		IOFunctions.println("Get Status..");
		JobConsumer consumerThread = new JobConsumer(new MyCallBack() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
			}

			@Override
			public void log(String log) {
				System.out.println("Log got: " + log);
				LogProcessingManager.processClusterLog(log);
			}
		});
		consumerThread.start();
	}

}
