package gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jcraft.jsch.JSchException;

import blockmanager.Block;
import blockmanager.BlocksManager;
import blockmanager.GraphicBlocksManager;
import clustering.MyCallBack;
import clustering.ScriptGenerator;
import clustering.jsch.SCP;
import tools.Config;
import tools.Helper;

public class PrgressParamsPanel extends JPanel {
	public List<Block> blocks;
	public static HashMap<Integer, Block> blockMap;
	private static final long serialVersionUID = -5489935889866505715L;
	public ArrayList<SliderPanel> sliderBoxSizePanel;
	public JLabel numberBlocksLabel;
	public SliderPanel sliderOverlapPanel;
	public Button startWorkFlowButton;
	public Button generateResultButton;
	public ProgressBarPanel progressBarPanel;
	public JTextField jobsField;

	public PrgressParamsPanel() {
		numberBlocksLabel = new JLabel("Total Blocks: 0", JLabel.CENTER);
		int sizes = Config.getDimensions().length;
		setLayout(new GridLayout(6 + sizes, 1, 20, 20));
		sliderBoxSizePanel = new ArrayList<SliderPanel>();
		for (int i = 0; i < sizes; i++) {
			SliderPanel slider = new SliderPanel(i, "Box Size[" + i + "]:", 100, 2000, 200);
			sliderBoxSizePanel.add(slider);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					GraphicBlocksManager.updateValues(Config.getDimensions());
					slider.updateValue((int) Config.getBlocksSize()[0]);
					numberBlocksLabel.setText("Total Blocks:" + Config.totalBlocks);
				}
			});
			t.start();
		}
		sliderOverlapPanel = new SliderPanel(-1, "Overlap:", 0, 200, 10);
		progressBarPanel = new ProgressBarPanel(0, 100);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				progressBarPanel.updateBar(Config.progressValue);
			}
		}, 0, 1000);
		startWorkFlowButton = new Button("START..");
		generateResultButton = new Button("Get and Generate Result");
	
		jobsField = new JTextField("10");
		JPanel jobsPanel = new JPanel();
		jobsPanel.setLayout(new GridLayout(1, 2, 10, 10));
		Label jobLabel = new Label("Jobs:");
		jobLabel.setAlignment(Label.RIGHT);
		jobsPanel.add(jobLabel);
		jobsPanel.add(jobsField);
		this.add(progressBarPanel);
		this.add(numberBlocksLabel);
		for (SliderPanel slider : sliderBoxSizePanel) {
			this.add(slider);
		}
		this.add(sliderOverlapPanel);
		this.add(jobsPanel);
		this.add(startWorkFlowButton);
		this.add(generateResultButton);

		startWorkFlowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				sendTask(new MyCallBack() {

					@Override
					public void onSuccess() {
						generateInput(new MyCallBack() {

							@Override
							public void onSuccess() {
								sendInput(new MyCallBack() {
									
									@Override
									public void onSuccess() {
										generateScript(new MyCallBack() {
											
											@Override
											public void onSuccess() {
												sendScript(new MyCallBack() {
													
													@Override
													public void onSuccess() {
														runScript(new MyCallBack() {
															
															@Override
															public void onSuccess() {
																// TODO Auto-generated method stub
																
															}
															
															@Override
															public void onError(String error) {
																// TODO Auto-generated method stub
																
															}
														});
														
													}
													
													@Override
													public void onError(String error) {
														// TODO Auto-generated method stub
														
													}
												});
												
											}
											
											@Override
											public void onError(String error) {
												// TODO Auto-generated method stub
												
											}
										});
										
									}
									
									@Override
									public void onError(String error) {
										// TODO Auto-generated method stub
										
									}
								});

							}

							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		generateResultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread task = new Thread(new Runnable() {

					@Override
					public void run() {
						getDataBack(new MyCallBack() {
							
							@Override
							public void onSuccess() {
								combineData(new MyCallBack() {
									
									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onError(String error) {
										// TODO Auto-generated method stub
										
									}
								});
								
							}
							
							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
				task.start();
			}
		});
	}

	private void sendTask(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Send Task..");
				Config.progressValue = 0;
				Config.setClusterTaskPath(Config.getClusterPath()
						+ Config.getLocalTaskPath().split("/")[Config.getLocalTaskPath().split("/").length - 1]);
				System.out.println(Config.getClusterTaskPath());
				try {
					SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getLocalTaskPath(),
							Config.getClusterTaskPath(), -1);
				} catch (JSchException e) {
					// TODO Fix Connection
					e.printStackTrace();
					callBack.onError(e.toString());
				} catch (IOException e) {
					// TODO retry
					e.printStackTrace();
					callBack.onError(e.toString());
				}
				Config.progressValue = 100;
				callBack.onSuccess();
			}
		});
		task.start();
	}

	private void generateInput(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Generate input blocks..");
				Config.progressValue = 0;
				blocks = BlocksManager.generateBlocks(Config.getInputFile(), Config.getBlocksSize(),
						Config.getOverlap());
				blockMap = BlocksManager.saveBlocks(Config.getInputFile(), blocks);
				Config.setBlocks(blockMap.size());
				callBack.onSuccess();
			}
		});
		task.run();
	}

	private void sendInput(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Send input files..");
				Config.progressValue = 0;
				String local = Config.getTempFolderPath();
				ArrayList<String> files = Helper.getFiles(local, Config.getInputPrefix());
				Config.setBlocksFilesNames(files);
				int key = 0;
				for (String file : files) {
					try {
						SCP.send(Config.getPseudo(), Config.getHost(), Config.getPort(), local + "//" + file,
								Config.getClusterPath() + "//" + file, key);
					} catch (JSchException | IOException e) {
						callBack.onError(e.toString());
					}
					key++;
					Config.progressValue = (key * 100) / files.size();
				}
				callBack.onSuccess();
			}
		});
		task.run();
	}

	private void generateScript(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Generate Script..");
				Config.progressValue = 0;
				int jobs;
				try {
					jobs = Integer.parseInt(jobsField.getText());
				} catch (Exception e) {
					Helper.log("Invalide Task number! putted default 10 Jobs");
					jobs = 10;

				}
				String[] localBlocksfiles = new File(Config.getTempFolderPath()).list();
				System.out.println();
				for (int i = 0; i < localBlocksfiles.length; i++)
					System.out.print(localBlocksfiles[i]);
				System.out.println();
				List<String[]> blocksPerjob = Helper.generateBlocksPerJob(localBlocksfiles, jobs);
				for (int i = 0; i < blocksPerjob.size(); i++) {
					Config.progressValue = (i * 100) / blocksPerjob.size();
					final int key = i;
					String scriptPath;
					try {
						scriptPath = ScriptGenerator.generateScript(
								Config.getLocalTaskPath().split("/")[Config.getLocalTaskPath().split("/").length - 1],
								blocksPerjob.get(key), key);
						Helper.log("Scripts generated");
						Helper.log("Send Scripts..");
						String scriptFileName = scriptPath.split("/")[scriptPath.split("/").length - 1];
						Config.addScriptFile(scriptFileName);
						callBack.onSuccess();
					} catch (FileNotFoundException e) {
						callBack.onError(e.toString());
					}

				}

			}
		});
		task.run();
	}

	private void sendScript(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Send Script..");
				for (String script : Config.getScriptFiles()) {
					try {
						SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getTempFolderPath()+"//"+ script,
								Config.getClusterPath()+ script, -1);
						callBack.onSuccess();
					} catch (JSchException e) {
						callBack.onError(e.toString());
						e.printStackTrace();
					} catch (IOException e) {
						callBack.onError(e.toString());
						e.printStackTrace();
					}
				}
			}
		});
		task.run();

	}

	private void runScript(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Run Script..");
				for (String scriptFile : Config.getScriptFiles()) {
					try {
						SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), scriptFile);
					} catch (JSchException e) {
						callBack.onError(e.toString());
						e.printStackTrace();
					}

				}
				callBack.onSuccess();
			}
		});
		task.run();
	}

	private void getDataBack(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Get Data back..");
				Config.progressValue = 0;
				ArrayList<String> files = Config.getBlocksFilesNames();
				int key = 0;
				for (String file : files) {
					try {
						SCP.get(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput() + "//" + file,
								Config.getTempFolderPath() + "//" + file, key);
						key++;
						Config.progressValue = (key * 100) / files.size();
					} catch (IOException e) {
						callBack.onError(e.toString());
						e.printStackTrace();
					} catch (JSchException e) {
						callBack.onError(e.toString());
						e.printStackTrace();
					}
					
				}
				callBack.onSuccess();
			}
		});
		task.run();
	}

	private void combineData(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Helper.log("Generate result..");
				BlocksManager.generateResult(blockMap, Config.getTempFolderPath());
				callBack.onSuccess();
			}
		});
		task.run();

	}

}