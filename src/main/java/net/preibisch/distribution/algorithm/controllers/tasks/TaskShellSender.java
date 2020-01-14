package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class TaskShellSender implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {


		System.out.println("TODO ! Task Shell sender");
//		Boolean valid = true;
//		MyLogger.log.info("Send Shell..");
//		try {
//			
//			System.out.println("Local task sh:"+Config.getTempFolderPath() + "//task.sh");
//			System.out.println("Cloud task sh:"+Config.getLogin().getServer().getPath() + "task.sh");
//			SCP.send(Config.getLogin(), "tools//logProvider.sh",
//					Config.getLogin().getServer().getPath() + "logProvider.sh", -1);
//			SCP.send(Config.getLogin(), "tools//logProvider.jar",
//					Config.getLogin().getServer().getPath() + "logProvider.jar", -1);
//			SCP.send(Config.getLogin(), "tools//task.sh",
//					Config.getLogin().getServer().getPath() + "task.sh", -1);
//		} catch (JSchException e) {
//			valid = false;
//			callback.onError(e.toString());
//			e.printStackTrace();
//			try {
//				SCP.connect(Config.getLogin());
//			} catch (JSchException e1) {
//				MyLogger.log.info("Invalide Host");
//				e1.printStackTrace();
//			}
//		} catch (IOException e) {
//			callback.onError(e.toString());
//			e.printStackTrace();
//		}
//
//		if (valid)
//			callback.onSuccess(pos);
//	
//		
	}

}
