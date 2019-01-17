package main.java.com.clustering.workflow;

import com.jcraft.jsch.JSchException;

import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.jsch.SCP;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class WorkerNodeManager {
	public static void runBatch(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Run Submit..");
				try {
					SCP.run(Config.getLogin(), "submit.cmd", callback);
				} catch (JSchException e) {
					try {
						SCP.connect(Config.getLogin());
					} catch (JSchException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					valid = false;
					callback.onError(e.toString());
					e.printStackTrace();
				}

				if (valid)
					callback.onSuccess();
			}
		});
		task.run();
	}

}
