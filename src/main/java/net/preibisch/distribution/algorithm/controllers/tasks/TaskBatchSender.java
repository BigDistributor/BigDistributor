package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPFunctions;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;

public class TaskBatchSender implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) throws JSchException, IOException {

		Boolean valid = true;
		callback.log("Send submit..");

		String localBatch = Job.getTmpDir() + "//submit.cmd";
		String serverBatch = Login.getServer().getPath() + "submit.cmd";
		System.out.println("Local Batch: ");

		SCPFunctions.sendFile(localBatch, serverBatch, -1);

		if (valid)
			callback.onSuccess(pos);

	}

}
