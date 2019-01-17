package main.java.com.gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.com.blockmanager.GraphicBlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.workflow.DataGetter;
import main.java.com.clustering.workflow.InputManager;
import main.java.com.clustering.workflow.ResultManager;
import main.java.com.clustering.workflow.ScriptManager;
import main.java.com.clustering.workflow.StatusListenerManager;
import main.java.com.clustering.workflow.TaskManager;
import main.java.com.clustering.workflow.WorkerNodeManager;
import main.java.com.clustering.workflow.Workflow;
import main.java.com.controllers.items.AppMode;
import main.java.com.tools.Config;
import main.java.com.tools.Helper;
import main.java.com.tools.IOFunctions;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5489935889866505715L;

//	public ParamsPanel paramsPanel;
	public Button startWorkFlowButton;
	public Button generateResultButton;
	public Workflow workflow;
	public InputPanel inputPanel;
	public SliderPanel sliderOverlapPanel;
	public JLabel numberBlocksLabel;
	public JTextField jobsField;
	public BlockSizeControlPanel blockSizeControlPanel;
	

	public ControlPanel(int dimensions) {
		workflow = new Workflow();
		StatusListenerManager.startStatusListener();
		blockSizeControlPanel = new BlockSizeControlPanel(Config.getDataPreview().getFile().getDimensions().length);

		numberBlocksLabel = new JLabel("Total Blocks: 0", JLabel.CENTER);

		setLayout(new GridLayout(10,1, 20, 20));
		sliderOverlapPanel = new SliderPanel(-1, "Overlap:", 0, 200, 10);

		startWorkFlowButton = new Button("START..");
		generateResultButton = new Button("Get and Generate Result");

		jobsField = new JTextField("10");
		JPanel jobsPanel = new JPanel();
		jobsPanel.setLayout(new GridLayout(1, 2, 10, 10));
		Label jobLabel = new Label("Block per job:");
		jobLabel.setAlignment(Label.RIGHT);
		jobsPanel.add(jobLabel);
		jobsPanel.add(jobsField);
		this.add(workflow.progressBarPanel);
		this.add(Helper.createImagePanel("img/labels.png"));

		this.add(numberBlocksLabel);
		for (SliderPanel slider : blockSizeControlPanel.sliderPanels) {
			this.add(slider);
		}
		this.add(sliderOverlapPanel);
		this.add(jobsPanel);
		this.add(startWorkFlowButton);
		this.add(generateResultButton);

		startWorkFlowButton.addActionListener(this);

		generateResultButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startWorkFlowButton) {
			if ( AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
				TaskManager.sendTask(new MyCallBack() {

					@Override
					public void onSuccess() {
						ScriptManager.generateShell(new MyCallBack() {

							@Override
							public void onSuccess() {
								ScriptManager.sendShell(new MyCallBack() {

									@Override
									public void onSuccess() {
										ScriptManager.generateBatch(Config.parallelJobs, new MyCallBack() {

											@Override
											public void onSuccess() {
												ScriptManager.sendBatch(new MyCallBack() {

													@Override
													public void onSuccess() {
														WorkerNodeManager.runBatch(new MyCallBack() {

															@Override
															public void onSuccess() {
															}

															@Override
															public void onError(String error) {
																// TODO Auto-generated method stub

															}

															@Override
															public void log(String log) {
																// TODO Auto-generated method stub

															}
														});

													}

													@Override
													public void onError(String error) {
														// TODO Auto-generated method stub

													}

													@Override
													public void log(String log) {
														// TODO Auto-generated method stub

													}
												});

											}

											@Override
											public void onError(String error) {
												// TODO Auto-generated method stub

											}

											@Override
											public void log(String log) {
												// TODO Auto-generated method stub

											}
										});
									}

									@Override
									public void onError(String error) {
										// TODO Auto-generated method stub

									}

									@Override
									public void log(String log) {
										// TODO Auto-generated method stub

									}
								});

							}

							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub

							}

							@Override
							public void log(String log) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void log(String log) {
						// TODO Auto-generated method stub

					}
				});
			}
			if ( AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {

				try {
					Config.parallelJobs = Integer.parseInt(jobsField.getText());
				} catch (Exception ex) {
					mpicbg.spim.io.IOFunctions.println("Invalide Task number! putted default 10 Jobs");
					Config.parallelJobs = 10;
				}
				TaskManager.sendTask(new MyCallBack() {

					@Override
					public void onSuccess() {
						InputManager.generateInput(new MyCallBack() {

							@Override
							public void onSuccess() {
								InputManager.sendInput(new MyCallBack() {

									@Override
									public void onSuccess() {
										ScriptManager.generateShell(new MyCallBack() {

											@Override
											public void onSuccess() {
												ScriptManager.sendShell(new MyCallBack() {

													@Override
													public void onSuccess() {
														ScriptManager.generateBatch(Config.parallelJobs, new MyCallBack() {

															@Override
															public void onSuccess() {
																ScriptManager.sendBatch(new MyCallBack() {

																	@Override
																	public void onSuccess() {
																		WorkerNodeManager.runBatch(new MyCallBack() {

																			@Override
																			public void onSuccess() {
																				// workflow.cleanFolder(
																				// Config.getTempFolderPath(),
																				// new MyCallBack() {
																				// @Override
																				// public void onSuccess() {
																				// // TODO Auto-generated
																				// // method stub
																				//
																				// }
																				//
																				// @Override
																				// public void onError(
																				// String error) {
																				// // TODO Auto-generated
																				// // method stub
																				//
																				// }
																				//
																				// @Override
																				// public void log(
																				// String log) {
																				// // TODO Auto-generated
																				// // method stub
																				//
																				// }
																				// });

																			}

																			@Override
																			public void onError(String error) {
																				// TODO Auto-generated method stub

																			}

																			@Override
																			public void log(String log) {
																				// TODO Auto-generated method stub

																			}
																		});

																	}

																	@Override
																	public void onError(String error) {
																		// TODO Auto-generated method stub

																	}

																	@Override
																	public void log(String log) {
																		// TODO Auto-generated method stub

																	}
																});

															}

															@Override
															public void onError(String error) {
																// TODO Auto-generated method stub

															}

															@Override
															public void log(String log) {
																// TODO Auto-generated method stub

															}
														});
													}

													@Override
													public void onError(String error) {
														// TODO Auto-generated method stub

													}

													@Override
													public void log(String log) {
														// TODO Auto-generated method stub

													}
												});

											}

											@Override
											public void onError(String error) {
												// TODO Auto-generated method stub

											}

											@Override
											public void log(String log) {
												// TODO Auto-generated method stub

											}
										});

									}

									@Override
									public void onError(String error) {
										// TODO Auto-generated method stub

									}

									@Override
									public void log(String log) {
										// TODO Auto-generated method stub

									}
								});

							}

							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub

							}

							@Override
							public void log(String log) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void log(String log) {
						// TODO Auto-generated method stub

					}
				});
			}
		}
		if (e.getSource() == generateResultButton) {
			if ( AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
				DataGetter.getAllDataBack(new MyCallBack() {

					@Override
					public void onSuccess() {
						ImageJFunctions.show(IOFunctions.openAs32Bit(new File(Config.getTempFolderPath()+"/file.tif"))).setTitle("Result");

					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void log(String log) {
						// TODO Auto-generated method stub

					}
				});

			} else if ( AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
						DataGetter.getAllDataBack(new MyCallBack() {

							@Override
							public void onSuccess() {
								ResultManager.combineData(new MyCallBack() {

									@Override
									public void onSuccess() {
										//TODO
//										ImageJFunctions.show(Config.resultImage).setTitle("Result");

									}

									@Override
									public void onError(String error) {
										// TODO Auto-generated method stub

									}

									@Override
									public void log(String log) {
										// TODO Auto-generated method stub

									}
								});

							}

							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub

							}

							@Override
							public void log(String log) {
								// TODO Auto-generated method stub

							}
						});
			}

		}
	}
}