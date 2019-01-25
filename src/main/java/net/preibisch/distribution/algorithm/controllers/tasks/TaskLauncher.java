package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;

public class TaskLauncher implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		Boolean valid = true;
		callback.log("Run Submit..");
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
			callback.onSuccess(pos);
	
		
	}

}
