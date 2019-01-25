package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.tools.Config;

public class AllDataGetter implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
			Boolean valid = true;
			System.out.println("Get Data back..");
			Workflow.progressBarPanel.updateBar(0);
			try {

				SCP.get(Config.getLogin(), Config.getJob().getInput().getFile().getAll(),
						Config.getTempFolderPath() + "//file.tif", 0);
				MyLogger.log.info("file got with success !");
				Workflow.progressBarPanel.updateBar(1);

			} catch (IOException e) {
				valid = false;
				callback.onError(e.toString());
				e.printStackTrace();
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
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				// callBack.onSuccess();
			}

			if (valid)
				callback.onSuccess(pos);
		} else if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
			Boolean valid = true;
			System.out.println("Get Data back..");
			Workflow.progressBarPanel.updateBar(0);
			// ArrayList<String> files = Config.getBlocksFilesNames();
			// int key = 0;
			for (int i = 1; i <= Config.getTotalInputFiles(); i++) {
				String file = i + Config.getInputPrefix();
				try {
					SCP.get(Config.getLogin(), Config.getLogin().getServer().getPath()+ "//" + file,
							Config.getTempFolderPath() + "//" + file, i - 1);

					MyLogger.log.info("block " + i + " got with success !");
					Workflow.progressBarPanel.updateBar((i * 100) / Config.getTotalInputFiles());
				} catch (IOException e) {
					valid = false;
					callback.onError(e.toString());
					e.printStackTrace();
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
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					// callBack.onSuccess();
				}

			}
			if (valid)
				callback.onSuccess(pos);
		}
	
	}

}
