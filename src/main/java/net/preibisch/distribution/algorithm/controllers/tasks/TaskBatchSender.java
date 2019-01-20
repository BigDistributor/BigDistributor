package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class TaskBatchSender implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {

		Boolean valid = true;
		IOFunctions.println("Send submit..");
		try {
			SCP.send(Config.getLogin(), Config.getTempFolderPath() + "//submit.cmd",
					Config.getLogin().getServer().getPath() + "submit.cmd", -1);
		} catch (JSchException e) {
			valid = false;
			callback.onError(e.toString());
			e.printStackTrace();
			try {
				SCP.connect(Config.getLogin());
			} catch (JSchException e1) {
				IOFunctions.println("Invalide Host");
				e1.printStackTrace();
			}
		} catch (IOException e) {
			callback.onError(e.toString());
			e.printStackTrace();
		}

		if (valid)
			callback.onSuccess(pos);
	
	}

}
