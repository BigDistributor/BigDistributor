package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;


public class DataGetter {


	public static void getBlockDataBack(int start, int end, AbstractCallBack callBack) {
//		Thread task = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				if (AppMode.CLUSTER_INPUT_MODE.equals(Job.getAppMode())) {
//					throw new Error("Cluster blocks get not implimented yet");
//				} else if (AppMode.LOCAL_INPUT_MODE.equals(Config.getJob().getAppMode())) {
//					Boolean valid = true;
//					callBack.log("Get Data back..");
//					Workflow.progressBarPanel.updateBar(0);
//					// ArrayList<String> files = Config.getBlocksFilesNames();
//					// int key = 0;
//					for (int i = start; i <= end; i++) {
//						String file = i + Config.getInputPrefix();
//						try {
//							SCP.get(Config.getLogin(), Config.getLogin().getServer().getPath() + "//" + file,
//									Config.getTempFolderPath() + "//" + file, i - 1);
//
//							callBack.log("block " + i + " got with success !");
//							Workflow.progressBarPanel.updateBar((i * 100) / Config.getTotalInputFiles());
//						} catch (IOException e) {
//							valid = false;
//							callBack.onError(e.toString());
//							e.printStackTrace();
//						} catch (JSchException e) {
//							try {
//								SCP.connect(Config.getLogin());
//							} catch (JSchException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//							valid = false;
//							callBack.onError(e.toString());
//							e.printStackTrace();
//						} catch (IndexOutOfBoundsException e) {
//							e.printStackTrace();
//							// callBack.onSuccess();
//						}
//
//					}
//					if (valid)
//						callBack.onSuccess(0);
//				}
//			}
//		});
//		task.run();
	}

}
