package main.java.com.controllers.tasks;

import main.java.com.clustering.kafka.JobConsumer;
import main.java.com.controllers.items.callback.AbstractCallBack;
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
