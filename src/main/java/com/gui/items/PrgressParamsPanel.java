package main.java.com.gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.com.blockmanager.GraphicBlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.WorkflowFunction;
import main.java.com.tools.Config;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class PrgressParamsPanel extends JPanel {
	private static final long serialVersionUID = -5489935889866505715L;
	public ArrayList<SliderPanel> sliderBoxSizePanel;
	public JLabel numberBlocksLabel;
	public SliderPanel sliderOverlapPanel;
	public Button startWorkFlowButton;
	public Button generateResultButton;
	public JTextField jobsField;
	public WorkflowFunction workflow;

	public PrgressParamsPanel() {
		workflow = new WorkflowFunction();
		workflow.startStatusListener();

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
					GraphicBlocksManager.updateValues(Config.getDimensions(), new MyCallBack() {

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
							workflow.logPanel.addText(log);

						}
					});
					slider.updateValue((int) Config.getBlocksSize()[0]);
					numberBlocksLabel.setText("Total Blocks:" + Config.totalBlocks);
				}
			});
			t.start();
		}
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

				workflow.sendTask(new MyCallBack() {

					@Override
					public void onSuccess() {
						workflow.generateInput(new MyCallBack() {

							@Override
							public void onSuccess() {
								workflow.sendInput(new MyCallBack() {

									@Override
									public void onSuccess() {
										workflow.generateShell(new MyCallBack() {

											@Override
											public void onSuccess() {
												workflow.sendShell(new MyCallBack() {

													@Override
													public void onSuccess() {
														int jobs;
														try {
															jobs = Integer.parseInt(jobsField.getText());
														} catch (Exception e) {
															workflow.logPanel.addText(
																	"Invalide Task number! putted default 10 Jobs");
															jobs = 10;
														}
														workflow.generateBatch(jobs, new MyCallBack() {

															@Override
															public void onSuccess() {
																workflow.sendBatch(new MyCallBack() {

																	@Override
																	public void onSuccess() {
																		workflow.runBatch(new MyCallBack() {

																			@Override
																			public void onSuccess() {
//																				workflow.cleanFolder(
//																						Config.getTempFolderPath(),
//																						new MyCallBack() {
//																							@Override
//																							public void onSuccess() {
//																								// TODO Auto-generated
//																								// method stub
//
//																							}
//
//																							@Override
//																							public void onError(
//																									String error) {
//																								// TODO Auto-generated
//																								// method stub
//
//																							}
//
//																							@Override
//																							public void log(
//																									String log) {
//																								// TODO Auto-generated
//																								// method stub
//
//																							}
//																						});

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
		});

		generateResultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread task = new Thread(new Runnable() {

					@Override
					public void run() {
						workflow.getDataBack(new MyCallBack() {

							@Override
							public void onSuccess() {
								workflow.combineData(new MyCallBack() {

									@Override
									public void onSuccess() {
										ImageJFunctions.show(Config.resultImg).setTitle("Result");

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
				});
				task.start();
			}
		});
	}
}