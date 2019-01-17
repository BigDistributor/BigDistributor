package main.java.com.clustering.workflow;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.jsch.SCP;
import main.java.com.clustering.scripting.BatchGenerator;
import main.java.com.clustering.scripting.ShellGenerator;
import main.java.com.controllers.items.AppMode;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class ScriptManager {
	public static void generateShell(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				IOFunctions.println("Generate Script..");
				ShellGenerator.generateTaskShell(callback);
			}
		});
		task.run();
	}

	public static void generateBatch(int job, MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				IOFunctions.println("Generate Batch..");
				if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
					BatchGenerator.GenerateBatchForLocalFiles(job, Config.getTotalInputFiles(), callback);
				} else if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
					BatchGenerator.GenerateBatchForClusterFile(callback);
				}
			}
		});
		task.run();
	}
	

	public static void sendShell(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Send Shell..");
				try {
					
					System.out.println("Local task sh:"+Config.getTempFolderPath() + "//task.sh");
					System.out.println("Cloud task sh:"+Config.getLogin().getServer().getPath() + "task.sh");
					SCP.send(Config.getLogin(), "tools//logProvider.sh",
							Config.getLogin().getServer().getPath() + "logProvider.sh", -1);
					SCP.send(Config.getLogin(), "tools//logProvider.jar",
							Config.getLogin().getServer().getPath() + "logProvider.jar", -1);
					SCP.send(Config.getLogin(), "tools//task.sh",
							Config.getLogin().getServer().getPath() + "task.sh", -1);
				} catch (JSchException e) {
					valid = false;
					callBack.onError(e.toString());
					e.printStackTrace();
					try {
						SCP.connect(Config.getLogin());
					} catch (JSchException e1) {
						IOFunctions.println("Invalide Host");
						e1.printStackTrace();
					}
				} catch (IOException e) {
					callBack.onError(e.toString());
					e.printStackTrace();
				}

				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}

	public static void sendBatch(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Send submit..");
				try {
					SCP.send(Config.getLogin(), Config.getTempFolderPath() + "//submit.cmd",
							Config.getLogin().getServer().getPath() + "submit.cmd", -1);
				} catch (JSchException e) {
					valid = false;
					callBack.onError(e.toString());
					e.printStackTrace();
					try {
						SCP.connect(Config.getLogin());
					} catch (JSchException e1) {
						IOFunctions.println("Invalide Host");
						e1.printStackTrace();
					}
				} catch (IOException e) {
					callBack.onError(e.toString());
					e.printStackTrace();
				}

				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}


}
