package gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import blockmanager.Block;
import blockmanager.BlocksManager;
import clustering.ScriptGenerator;
import clustering.jsch.SCP;
import tools.Config;
import tools.Helper;

public class PrgressParamsPanel extends JPanel {
	private static final long serialVersionUID = -5489935889866505715L;
	public Scrollbar sliderX;
	public Scrollbar sliderY;
	public Button sendJarButton;
	public Button generateInputButton;
	public Button sendInputButton;
	public Button generateScriptButton;
	public Button runScriptButton;
	public Button getDataButton;
	public Button combinData;
	public List<Block> blocks;
	public JTextField jobsField;
	public static HashMap<Integer, Block> blockMap;

	public PrgressParamsPanel() {
		setLayout(new GridLayout(10, 1, 20, 20));
		sliderX = new Scrollbar(Scrollbar.HORIZONTAL, 100, 1, 100, 1000);
		sliderY = new Scrollbar(Scrollbar.HORIZONTAL, 8, 1, 1, 21);
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

		this.add(sliderX);
		this.add(sliderY);
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
				System.out.println("Send Jar clicked");
				Config.setClusterJar(Config.getClusterPath()
						+ Config.getLocalJar().split("/")[Config.getLocalJar().split("/").length - 1]);
				System.out.println(Config.getClusterJar());
				SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getLocalJar(), Config.getClusterJar(), -1);
			}
		});

		generateInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blocks = BlocksManager.generateBlocks(Config.getLocalInput(), 100, 8);
				blockMap = BlocksManager.saveBlocks(Config.getLocalInput(), blocks);
				Config.setBlocks(blockMap.size());
			}
		});

		sendInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send input files clicked");
				SCP.sendFolder(Config.getPseudo(), Config.getHost(), 22, Config.getInputTempDir(),
						Config.getClusterInput());
			}
		});
		generateScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int jobs = 10;
				System.out.println("generate config.sh clicked");
				try {
					jobs = Integer.parseInt(jobsField.getText());
				} catch (Exception exc) {
					Helper.log("Invalide Jobs input");
				} finally {
					String[] localBlocksfiles = new File(Config.getInputTempDir()).list();
					List<String[]> blocksPerjob = Helper.generateBlocksPerJob(localBlocksfiles, jobs);
					for (int i = 0; i < blocksPerjob.size(); i++) {
						final int key = i;
						Thread task = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									
									String scriptPath = ScriptGenerator.generateScript(
											Config.getLocalJar().split("/")[Config.getLocalJar().split("/").length - 1],
											blocksPerjob.get(key), key);
									System.out.println("Script generated: " + scriptPath);
									System.out.println("Sending Script..");
									String clusterScript = scriptPath.split("/")[scriptPath.split("/").length - 1];
									System.out.println("local Script: " + scriptPath);
									System.out.println("Cluster Script: " + clusterScript);
									Config.addScriptFile(clusterScript);
									SCP.send(Config.getPseudo(), Config.getHost(), 22, scriptPath,
											Config.getClusterPath() + clusterScript, -1);
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								}
							}
						});
						task.run();
					}
				}
			}

		});
		runScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("run config.sh clicked");
				for(String scriptFile:Config.getScriptFiles()) {
				SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), scriptFile);
				}
			}
		});

		getDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("get data clicked");
				SCP.getFolder(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInput(),
						Config.getInputTempDir());
			}
		});

		combinData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlocksManager.generateResult(blockMap, Config.getInputTempDir());

			}
		});
	}

}
