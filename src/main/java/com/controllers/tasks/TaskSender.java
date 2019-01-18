package main.java.com.clustering.workflow;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.jsch.SCP;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class TaskManager {
	public static void sendTask(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Send Task..");
				Workflow.progressBarPanel.updateBar(0);
				System.out.println("Task in cloud: "+ Config.getJob().getTask().getAll());
				System.out.println("Task in local: "+Config.getLogin().getServer().getPath() + "/task.jar");
				try {
					SCP.send(Config.getLogin(), Config.getJob().getTask().getAll(),
							Config.getLogin().getServer().getPath() + "/task.jar", -1);
				} catch (JSchException e) {
					valid = false;
					// TODO Fix Connection
					e.printStackTrace();
					callBack.onError(e.toString());
				} catch (IOException e) {
					// TODO retry
					valid = false;
					e.printStackTrace();
					callBack.onError(e.toString());
				}
				Workflow.progressBarPanel.updateBar(100);
				if (valid)
					callBack.onSuccess();
			}
		});
		task.start();
	}

}
