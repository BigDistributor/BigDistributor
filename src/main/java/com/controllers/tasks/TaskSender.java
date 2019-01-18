package main.java.com.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.com.clustering.jsch.SCP;
import main.java.com.clustering.workflow.Workflow;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class TaskSender implements AbstractTask {

	@Override
	public void start(int pos,AbstractCallBack callback) {
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
			callback.onError(e.toString());
		} catch (IOException e) {
			// TODO retry
			valid = false;
			e.printStackTrace();
			callback.onError(e.toString());
		}
		Workflow.progressBarPanel.updateBar(100);
		if (valid)
			callback.onSuccess(pos);
		
	}

}
