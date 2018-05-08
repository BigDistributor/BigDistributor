package gui.items;

import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import blockmanager.Block;
import blockmanager.BlocksManager;
import clustering.ScriptGenerator;
import clustering.jsch.SCP;
import tools.Config;

public class PrgressParamsPanel extends JPanel {
	private static final long serialVersionUID = -5489935889866505715L;
	public Scrollbar sliderX;
	public Scrollbar sliderY;
	public Button sendJarButton;
	public Button generateInputButton;
	public Button sendInputButton;
	public Button generateScriptButton;
	public Button sendScriptButton;
	public Button runScriptButton;
	public Button getDataButton;
	public Button combinData;
	public List< Block > blocks;
	public HashMap< Integer, Block > blockMap;

	public PrgressParamsPanel() {
		setLayout(new GridLayout(10, 1, 20, 20));
		sliderX = new Scrollbar(Scrollbar.HORIZONTAL, 100, 1, 100, 1000);
		sliderY = new Scrollbar(Scrollbar.HORIZONTAL, 8, 1, 1, 21);
		sendJarButton = new Button("Send Jar");
		generateInputButton = new Button("Generate Input");
		sendInputButton = new Button("Send Input");
		generateScriptButton = new Button("Generate Script");
		sendScriptButton = new Button("Send Script");
		runScriptButton = new Button("Run Script");
		getDataButton = new Button("Get Data back");
		combinData = new Button("Generate Result");
		this.add(sliderX);
		this.add(sliderY);
		this.add(sendJarButton);
		this.add(generateInputButton);
		this.add(sendInputButton);
		this.add(generateScriptButton);
		this.add(sendScriptButton);
		this.add(runScriptButton);
		this.add(getDataButton);
		this.add(combinData);

		sendJarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send Jar clicked");
				 Config.setClusterJar(Config.getClusterPath() + Config.getLocalJar().split("/")[Config.getLocalJar().split("/").length - 1]);
				System.out.println(Config.getClusterJar());
				SCP.send(Config.getPseudo(), Config.getHost(), 22, Config.getLocalJar(), Config.getClusterJar());
			}
		});
		
		generateInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blocks = BlocksManager.generateBlocks(Config.getLocalInput(), 100, 8);
				blockMap = BlocksManager.saveBlocks(Config.getLocalInput(), blocks);
			}
		});

		sendInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send input files clicked");
				SCP.sendFolder(Config.getPseudo(), Config.getHost(), 22, Config.getInputTempDir(), Config.getClusterInput());
				
			}
		});
		generateScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("generate config.sh clicked");
				try {
					String[] localBlocksfiles = new File(Config.getInputTempDir()).list();
					String scriptPath = ScriptGenerator.generateScript(Config.getLocalJar().split("/")[Config.getLocalJar().split("/").length - 1],
							localBlocksfiles);
					System.out.println("Script generated: " + scriptPath);
					System.out.println("Sending Script..");
					String clusterScript = scriptPath.split("/")[scriptPath.split("/").length - 1];
					System.out.println("local Script: " + scriptPath);
					System.out.println("Cluster Script: " + clusterScript);
					Config.setScriptFile(clusterScript);
//					Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getLocalInputString()));
//					ImageJFunctions.show(image).setTitle("Original");
					SCP.send(Config.getPseudo(), Config.getHost(), 22, scriptPath,
							Config.getClusterPath() + Config.getsSriptFile());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		runScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("run config.sh clicked");
				SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(), Config.getsSriptFile());
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
