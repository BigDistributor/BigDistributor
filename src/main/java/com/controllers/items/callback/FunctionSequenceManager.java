package main.java.com.clustering.workflow;

import main.java.com.clustering.MyCallBack;

enum Provider {
	sendTask,
	sendInput,
	send
}
public final class CallBackImplimentation  {

	public static MyCallBack callBack;
	
	public CallBackImplimentation() {
		callBack = new MyCallBack() {
			
			@Override
			public void onSuccess() {
//				Provider provider
				
			}

			@Override
			public void onError(String error) {
				MyLogger.log.error(error);
				
			}

			@Override
			public void log(String log) {
				MyLogger.log.info(log);
			}
		};
	}

}
