package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.AppMode;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;

public class AllDataGetter implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		if (AppMode.CLUSTER_INPUT_MODE.equals(Job.getAppMode())) {
			Boolean valid = true;
			System.out.println("Get Data back..");
			Workflow.progressBarPanel.updateBar(0);
//				SCPFunctions.getFile(Job.getInput().getAbsolutePath(),
//						Job.getTmpDir() + "//file.tif", 0);
				MyLogger.log.info("file got with success !");
				Workflow.progressBarPanel.updateBar(1);

		
			if (valid)
				callback.onSuccess(pos);
		} else if (AppMode.LOCAL_INPUT_MODE.equals(Job.getAppMode())) {
			Boolean valid = true;
			System.out.println("Get Data back..");
			Workflow.progressBarPanel.updateBar(0);
			// ArrayList<String> files = Config.getBlocksFilesNames();
			// int key = 0;
//			for (int i = 1; i <= Config.getTotalInputFiles(); i++) {
//				String file = i + Config.getInputPrefix();
//				try {
//					SCP.get(Config.getLogin(), Config.getLogin().getServer().getPath()+ "//" + file,
//							Config.getTempFolderPath() + "//" + file, i - 1);
//
//					MyLogger.log.info("block " + i + " got with success !");
//					Workflow.progressBarPanel.updateBar((i * 100) / Config.getTotalInputFiles());
//				} catch (IOException e) {
//					valid = false;
//					callback.onError(e.toString());
//					e.printStackTrace();
//				} catch (JSchException e) {
//					try {
//						
//					e.printStackTrace();
//				} catch (IndexOutOfBoundsException e) {
//					e.printStackTrace();
//					// callBack.onSuccess();
//				}
//
//			}
//			if (valid)
//				callback.onSuccess(pos);
		}
	
	}

}
