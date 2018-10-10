package main.java.com.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.JSchException;

import main.java.com.blockmanager.Block;
import main.java.com.blockmanager.BlocksManager;
import main.java.com.clustering.jsch.SCP;
import main.java.com.clustering.kafka.JobConsumer;
import main.java.com.clustering.scripting.BatchGenerator;
import main.java.com.clustering.scripting.ShellGenerator;
import main.java.com.gui.items.Colors;
import main.java.com.gui.items.LogPanel;
import main.java.com.gui.items.ProgressBarPanel;
import main.java.com.tools.AppMode;
import main.java.com.tools.Config;
import main.java.com.tools.Helper;

public class WorkflowFunction {
	public List<Block> blocks;
	public static HashMap<Integer, Block> blockMap;
	public ProgressBarPanel progressBarPanel;
	public LogPanel logPanel;

	public WorkflowFunction() {
		progressBarPanel = new ProgressBarPanel(0, 100);
		logPanel = new LogPanel();
	}

	public void sendTask(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Send Task..");
				progressBarPanel.updateBar(0);
				// Config.getClusterPath()+Config.getLocalTaskPath().split("/")[Config.getLocalTaskPath().split("/").length-
				// 1]
				Config.setClusterTaskPath(Config.getClusterPath() + "/task.jar");
				System.out.println("Task in cloud: "+ Config.getClusterTaskPath());
				System.out.println("Task in local: "+ Config.getLocalTaskPath());
				try {
					SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getLocalTaskPath(),
							Config.getClusterTaskPath(), -1);
				} catch (JSchException e) {
					valid = false;
					// TODO Fix Connection
					e.printStackTrace();
					callBack.onError(e.toString());
				} catch (IOException e) {
					// TODO retry
					valid = false;
					e.printStackTrace();
					callBack.onError(e.toString());
				}
				progressBarPanel.updateBar(100);
				if (valid)
					callBack.onSuccess();
			}
		});
		task.start();
	}

	public void cleanFolder(String dir, MyCallBack callback) {
		try {
			FileUtils.cleanDirectory(new File(dir));
		} catch (IOException e) {
			callback.onError(e.toString());
		}
		callback.onSuccess();
	}

	public void generateInput(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Generate input blocks..");
				progressBarPanel.updateBar(0);
				blocks = BlocksManager.generateBlocks(Config.getInputFile(), Config.getBlocksSize(),
						Config.getOverlap(), callback);
				Config.setTotalInputFiles(blocks.size());
				blockMap = BlocksManager.saveBlocks(Config.getInputFile(), blocks, new MyCallBack() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void log(String log) {
						logPanel.addText(log);

					}
				});
				Config.setBlocks(blockMap.size());
				if (valid)
					callback.onSuccess();
			}
		});
		task.run();
	}

	public void sendInput(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Send input files..");
				progressBarPanel.updateBar(0);
				String local = Config.getTempFolderPath();
				ArrayList<String> files = Helper.getFiles(local, Config.getInputPrefix());
				Config.setBlocksFilesNames(files);
				int key = 0;
				for (String file : files) {
					try {
						SCP.send(Config.getPseudo(), Config.getHost(), Config.getPort(), local + "//" + file,
								Config.getClusterPath() + "//" + file, key);
					} catch (JSchException | IOException e) {
						valid = false;
						callBack.onError(e.toString());
					}
					key++;
					progressBarPanel.updateBar((key * 100) / files.size());
				}
				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}

	public void generateShell(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				logPanel.addText("Generate Script..");
				ShellGenerator.generateTaskShell(callback);
			}
		});
		task.run();
	}

	public void generateBatch(int job, MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				logPanel.addText("Generate Batch..");
				if(Config.APP_MODE == AppMode.LocalInputMode) {
				BatchGenerator.GenerateBatchForLocalFiles(job, Config.getTotalInputFiles(), callback);}
				else if(Config.APP_MODE == AppMode.ClusterInputMode){
					BatchGenerator.GenerateBatchForClusterFile(callback);
				}
			}
		});
		task.run();
	}

	public void sendShell(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Send Shell..");
				try {
					
					System.out.println("Local task sh:"+Config.getTempFolderPath() + "//task.sh");
					System.out.println("Cloud task sh:"+Config.getClusterPath() + "task.sh");
					SCP.send(Config.getPseudo(), Config.getHost(), 22, "tools//logProvider.sh",
							Config.getClusterPath() + "logProvider.sh", -1);
					SCP.send(Config.getPseudo(), Config.getHost(), 22, "tools//logProvider.jar",
							Config.getClusterPath() + "logProvider.jar", -1);
					SCP.send(Config.getPseudo(), Config.getHost(), 22, "tools//task.sh",
							Config.getClusterPath() + "task.sh", -1);
				} catch (JSchException e) {
					valid = false;
					callBack.onError(e.toString());
					e.printStackTrace();
					try {
						SCP.connect(Config.getPseudo(), Config.getHost());
					} catch (JSchException e1) {
						logPanel.addText("Invalide Host");
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

	public void sendBatch(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Send submit..");
				try {
					SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getTempFolderPath() + "//submit.cmd",
							Config.getClusterPath() + "submit.cmd", -1);
				} catch (JSchException e) {
					valid = false;
					callBack.onError(e.toString());
					e.printStackTrace();
					try {
						SCP.connect(Config.getPseudo(), Config.getHost());
					} catch (JSchException e1) {
						logPanel.addText("Invalide Host");
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

	public void runBatch(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				logPanel.addText("Run Submit..");
				try {
					SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), "submit.cmd", callback);
				} catch (JSchException e) {
					try {
						SCP.connect(Config.getPseudo(), Config.getHost());
					} catch (JSchException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					valid = false;
					callback.onError(e.toString());
					e.printStackTrace();
				}

				if (valid)
					callback.onSuccess();
			}
		});
		task.run();
	}

	public void getDataBack(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				System.out.println("Get Data back..");
				progressBarPanel.updateBar(0);
//				ArrayList<String> files = Config.getBlocksFilesNames();
//				int key = 0;
				for (int i = 1; i<=Config.getTotalInputFiles();i++) {
					String file = i + Config.getInputPrefix(); 
					try {
							SCP.get(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput() + "//" + file,
								Config.getTempFolderPath() + "//" + file, i-1);

						logPanel.addText("block " + i + " got with success !");
						progressBarPanel.updateBar((i * 100) / Config.getTotalInputFiles());
					} catch (IOException e) {
						valid = false;
						callBack.onError(e.toString());
						e.printStackTrace();
					} catch (JSchException e) {
						try {
							SCP.connect(Config.getPseudo(), Config.getHost());
						} catch (JSchException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						valid = false;
						callBack.onError(e.toString());
						e.printStackTrace();
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
//						callBack.onSuccess();
					}

				}
				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}

	public void combineData(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				logPanel.addText("Generate result..");
				Config.resultImg = BlocksManager.generateResult(blockMap, Config.getTempFolderPath(), callBack);
				callBack.onSuccess();
			}
		});
		task.run();

	}

	public void startStatusListener() {
		logPanel.addText("Get Status..");
		JobConsumer consumerThread = new JobConsumer(new MyCallBack() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
			}

			@Override
			public void log(String log) {
				System.out.println("Log got: " + log);
				processClusterLog(log);
			}
		});
		consumerThread.start();
	}

	private void processClusterLog(String log) {
		String[] parts = log.split(";");
		try {
//			System.out.println("ProcessLog:"+ parts[1] +" - " + Config.getUUID()+" "+(parts[1] == Config.getUUID())+" "+parts[1].equals(Config.getUUID()));
			if (parts[1].equals(Config.getUUID()) ) {
				logPanel.addText("Log got:" + log);
				int id = Integer.parseInt(parts[2]);
				for (int j = (id-1)*Config.parallelJobs; j < id*Config.parallelJobs; j++) {
					Config.blocksView.get(j).setStatus(Colors.PROCESSED);
				}
			}
		} catch (Exception e) {
			System.out.println("converting log error");
		}
	}
}
