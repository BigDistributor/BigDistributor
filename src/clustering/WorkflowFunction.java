package clustering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.JSchException;

import blockmanager.Block;
import blockmanager.BlocksManager;
import clustering.jsch.SCP;
import clustering.kafka.JobConsumer;
import clustering.kafka.KafkaProperties;
import clustering.scripting.BatchGenerator;
import clustering.scripting.ShellGenerator;
import gui.items.LogPanel;
import gui.items.ProgressBarPanel;
import tools.Config;
import tools.Helper;

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
//				 Config.getClusterPath()+Config.getLocalTaskPath().split("/")[Config.getLocalTaskPath().split("/").length- 1]
				Config.setClusterTaskPath("task.jar");
				System.out.println(Config.getClusterTaskPath());
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
				callback.onSuccess();
			}
		});
		task.run();
	}

	public void generateBatch(int job, MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				logPanel.addText("Generate Batch..");
				BatchGenerator.GenerateBatch(job, Config.getTotalInputFiles(), callback);
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
						SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getTempFolderPath() + "//task.sh" ,
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
				logPanel.addText("Send Shell..");
					try {
						SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getTempFolderPath() + "//submit.cmd" ,
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
						SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), "submit.cmd",callback);
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
				ArrayList<String> files = Config.getBlocksFilesNames();
				int key = 0;
				for (String file : files) {
					try {
						SCP.get(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput() + "//" + file,
								Config.getTempFolderPath() + "//" + file, key);

						logPanel.addText("block " + key + " got with success !");
						key++;
						progressBarPanel.updateBar((key * 100) / files.size());
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
						callBack.onSuccess();
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
				BlocksManager.generateResult(blockMap, Config.getTempFolderPath(), callBack);
				callBack.onSuccess();
			}
		});
		task.run();

	}

	public void generateStatus(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				logPanel.addText("Get Status..");
				JobConsumer consumerThread = new JobConsumer(KafkaProperties.TOPIC_DONE_TASK,callback);
		        consumerThread.start();
			}
		});
		task.run();
	}

	public void getStatus(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				System.out.println("Get Status..");
				try {
					SCP.get(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput() + "//log.txt",
							Config.getTempFolderPath() + "//log.txt", -2);
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
				}
				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}

	public void showLog(MyCallBack callback) throws FileNotFoundException, IOException {
		try (FileInputStream inputStream = new FileInputStream(Config.getTempFolderPath() + "//log.txt")) {
			String everything = IOUtils.toString(inputStream);
			if (everything.length() <= 10)
				logPanel.addText("0 Task running");
			else
				logPanel.addText(everything);
		}
	}
}
