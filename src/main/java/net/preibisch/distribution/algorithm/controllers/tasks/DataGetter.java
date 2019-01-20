package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCP;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class DataGetter {


	public static void getBlockDataBack(int start, int end, AbstractCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
					throw new Error("Cluster blocks get not implimented yet");
				} else if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
					Boolean valid = true;
					System.out.println("Get Data back..");
					Workflow.progressBarPanel.updateBar(0);
					// ArrayList<String> files = Config.getBlocksFilesNames();
					// int key = 0;
					for (int i = start; i <= end; i++) {
						String file = i + Config.getInputPrefix();
						try {
							SCP.get(Config.getLogin(), Config.getLogin().getServer().getPath() + "//" + file,
									Config.getTempFolderPath() + "//" + file, i - 1);

							IOFunctions.println("block " + i + " got with success !");
							Workflow.progressBarPanel.updateBar((i * 100) / Config.getTotalInputFiles());
						} catch (IOException e) {
							valid = false;
							callBack.onError(e.toString());
							e.printStackTrace();
						} catch (JSchException e) {
							try {
								SCP.connect(Config.getLogin());
							} catch (JSchException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							valid = false;
							callBack.onError(e.toString());
							e.printStackTrace();
						} catch (IndexOutOfBoundsException e) {
							e.printStackTrace();
							// callBack.onSuccess();
						}

					}
					if (valid)
						callBack.onSuccess(0);
				}
			}
		});
		task.run();
	}

}
