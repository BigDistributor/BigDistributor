package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;
import java.util.ArrayList;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Helper;
import main.java.net.preibisch.distribution.tools.config.Config;

public class InputSender implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {

		Boolean valid = true;
		callback.log("Send input files..");
		Workflow.progressBarPanel.updateBar(0);
		String local = Config.getTempFolderPath();
		ArrayList<String> files = Helper.getFiles(local, Config.getInputPrefix());
		Config.setBlocksFilesNames(files);
		int key = 0;
		for (String file : files) {
			try {
				SCP.send(Config.getLogin(), local + "//" + file,
						Config.getLogin().getServer().getPath() + "//" + file, key);
			} catch (JSchException | IOException e) {
				valid = false;
				callback.onError(e.toString());
			}
			key++;
			Workflow.progressBarPanel.updateBar((key * 100) / files.size());
		}
		if (valid)
			callback.onSuccess(pos);
	
	}

}
