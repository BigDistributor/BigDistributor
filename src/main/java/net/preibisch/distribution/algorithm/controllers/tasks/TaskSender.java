package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;

public class TaskSender implements AbstractTask {

	@Override
	public void start(int pos,AbstractCallBack callback) {
		Boolean valid = true;
		callback.log("Send Task..");
		Workflow.progressBarPanel.updateBar(0);
		String taskClusterPath = Config.getLogin().getServer().getPath() + "/"+Job.TASK_CLUSTER_NAME;
		System.out.println("Task in cloud: "+ Config.getJob().getTask().getAll());
		System.out.println("Task in local: "+taskClusterPath );
		try {
			SCP.send(Config.getLogin(), Config.getJob().getTask().getAll(),
					taskClusterPath, -1);
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
