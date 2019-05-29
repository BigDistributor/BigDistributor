package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPFunctions;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;


public class TaskLauncher implements AbstractTask{
	private static final String SUBMIT_FILE = "submit.cmd";

	@Override
	public void start(int pos, AbstractCallBack callback) throws JSchException {

		Boolean valid = true;
		callback.log("Run Submit..");
		String command = "cd " + Job.getCluster().getPath() + " && chmod +x " + SUBMIT_FILE + " && ./"+ SUBMIT_FILE;
		
			SCPFunctions.runCommand( command);

		if (valid)
			callback.onSuccess(pos);
	
		
	}

}
