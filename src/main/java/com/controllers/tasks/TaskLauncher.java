package main.java.com.controllers.tasks;

import com.jcraft.jsch.JSchException;

import main.java.com.clustering.jsch.SCP;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class TaskLauncher implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

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
			callback.onSuccess(pos);
	
		
	}

}
