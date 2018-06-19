package gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JTextField;

import blockmanager.Block;
import blockmanager.BlocksManager;
import blockmanager.GraphicBlocksManager;
import clustering.ScriptGenerator;
import clustering.jsch.SCP;
import tools.Config;
import tools.Helper;

public class PrgressParamsPanel extends JPanel {
	private static final long serialVersionUID = -5489935889866505715L;
	public SliderPanel sliderBoxSizePanel;
	public SliderPanel sliderOverlapPanel;
	public Button sendJarButton;
	public Button generateInputButton;
	public Button sendInputButton;
	public Button generateScriptButton;
	public Button runScriptButton;
	public Button getDataButton;
	public Button combinData;
	public ProgressBarPanel progressBarPanel;
	public List<Block> blocks;
	public JTextField jobsField;
	public static HashMap<Integer, Block> blockMap;

	public PrgressParamsPanel() {
		setLayout(new GridLayout(11, 1, 20, 20));
		sliderBoxSizePanel = new SliderPanel("Box Size:", 100, 2000, 200);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				GraphicBlocksManager.updateValues(Config.getDimensions());
				sliderBoxSizePanel.updateValue(Config.getBlocksSize()[0]+"/"+Config.totalBlocks);
			}
		});
		t.start();
		sliderOverlapPanel = new SliderPanel("Overlap:", 0, 200, 10);
		progressBarPanel = new ProgressBarPanel(0, 100);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				progressBarPanel.updateBar(Config.progressValue);
			}
		}, 0, 1000);
		sendJarButton = new Button("Send Jar");
		generateInputButton = new Button("Generate Input");
		sendInputButton = new Button("Send Input");
		generateScriptButton = new Button("Generate Script");
		runScriptButton = new Button("Run Script");
		getDataButton = new Button("Get Data back");
		combinData = new Button("Generate Result");
		jobsField = new JTextField("10");
		JPanel jobsPanel = new JPanel();
		jobsPanel.setLayout(new GridLayout(1, 2, 10, 10));
		Label jobLabel = new Label("Jobs:");
		jobLabel.setAlignment(Label.RIGHT);
		jobsPanel.add(jobLabel);
		jobsPanel.add(jobsField);
		this.add(progressBarPanel);
		this.add(sliderBoxSizePanel);
		this.add(sliderOverlapPanel);
		this.add(sendJarButton);
		this.add(generateInputButton);
		this.add(sendInputButton);
		this.add(jobsPanel);
		this.add(generateScriptButton);
		this.add(runScriptButton);
		this.add(getDataButton);
		this.add(combinData);
		sendJarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread task = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("Send Jar clicked");
						Config.progressValue = 0;
						Config.setClusterTaskPath(Config.getClusterPath()
								+ Config.getLocalTaskPath().split("/")[Config.getLocalTaskPath().split("/").length - 1]);
						System.out.println(Config.getClusterTaskPath());
						SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getLocalTaskPath(),
								Config.getClusterTaskPath(), -1);
						Config.progressValue = 100;
					}
				});
				task.start();
			}
		});
		generateInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.progressValue = 0;
				blocks = BlocksManager.generateBlocks(Config.getInputFile(), Config.getBlocksSize(),
						Config.getOverlap());
				blockMap = BlocksManager.saveBlocks(Config.getInputFile(), blocks);
				Config.setBlocks(blockMap.size());
			}
		});
		sendInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send input files clicked");
				Config.progressValue = 0;
				SCP.sendFolder(Config.getPseudo(), Config.getHost(), 22, Config.getTempFolderPath(),
						Config.getClusterInput());
			}
		});
		generateScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Thread task = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							int jobs = 10;
							Config.progressValue = 0;
							System.out.println("generate config.sh clicked");

							jobs = Integer.parseInt(jobsField.getText());
							String[] localBlocksfiles = new File(Config.getTempFolderPath()).list();
							System.out.println();
							for (int i = 0; i < localBlocksfiles.length; i++)
								System.out.print(localBlocksfiles[i]);
							System.out.println();
							List<String[]> blocksPerjob = Helper.generateBlocksPerJob(localBlocksfiles, jobs);
							for (int i = 0; i < blocksPerjob.size(); i++) {
								Config.progressValue = (i * 100) / blocksPerjob.size();
								final int key = i;
								String scriptPath = ScriptGenerator
										.generateScript(
												Config.getLocalTaskPath()
														.split("/")[Config.getLocalTaskPath().split("/").length - 1],
												blocksPerjob.get(key), key);
								System.out.println("Script generated: " + scriptPath);
								System.out.println("Sending Script..");
								String clusterScript = scriptPath.split("/")[scriptPath.split("/").length - 1];
								System.out.println("local Script: " + scriptPath);
								System.out.println("Cluster Script: " + clusterScript);
								Config.addScriptFile(clusterScript);
								SCP.send(Config.getPseudo(), Config.getHost(), 22, scriptPath,
										Config.getClusterPath() + clusterScript, -1);
							}
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});
				task.run();
			}
		});
		runScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread task = new Thread(new Runnable() {

					@Override
					public void run() {
						System.out.println("run config.sh clicked");
						for (String scriptFile : Config.getScriptFiles()) {
							SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), scriptFile);
						}
					}
				});
				task.start();
			}
		});
		getDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("get data clicked");
				Config.progressValue = 0;
				SCP.getFolder(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput(),
						Config.getTempFolderPath());
			}
		});
		combinData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlocksManager.generateResult(blockMap, Config.getTempFolderPath());

			}
		});
	}
}
