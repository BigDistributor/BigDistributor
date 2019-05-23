package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPFunctions;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;


public class TaskSender implements AbstractTask {

	@Override
	public void start(int pos,AbstractCallBack callback) throws JSchException, IOException {
		MyLogger.log.info("Start task sending ..");
		Boolean valid = true;
		callback.log("Send Task..");
		Workflow.progressBarPanel.updateBar(0);
		String taskClusterPath = Login.getServer().getPath() + "/"+Job.TASK_CLUSTER_NAME;
		
		System.out.println("Task in local: "+ Job.getTask().getAbsolutePath());
		System.out.println("Task in cloud: "+taskClusterPath );
		
			SCPFunctions.sendFile(Job.getTask().getAbsolutePath(), taskClusterPath, -1);
		Workflow.progressBarPanel.updateBar(100);
		if (valid) {
			MyLogger.log.info("Finish task sending");
			callback.onSuccess(pos);}
		
	}

}
