package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import clustering.ScriptGenerator;
import clustering.jsch.SCP;
import fiji.util.gui.GenericDialogPlus;
import ij.plugin.PlugIn;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import tools.Config;
import tools.IOFunctions;

public class MainFrame_ implements PlugIn {

	public static String defaultfilename = "";
	public static String defaultPath = "";

	public void run(String arg) {
		// create a dialog with two numeric input fields
		GenericDialogPlus gd = new GenericDialogPlus("MDC Clustering");

		gd.addFileField("Jar File", defaultfilename, 65);
		gd.addFileField("Dir", defaultfilename, 65); // defaultPath addDirectoryField

		gd.addButton("Config", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GenericDialogPlus config = new GenericDialogPlus("Cluster config");
				config.addStringField("Host", "maxlogin2.mdc-berlin.net");
				config.addNumericField("Port", 22, 5);
				config.addStringField("Pseudo", "mzouink");
				config.addStringField("PW", "");
				config.showDialog();
				Config.init(config.getNextString(), (int) config.getNextNumber(), config.getNextString(),
						config.getNextString());
			}
		});

		gd.showDialog();
		// if (gd.wasCanceled()) {
		// IJ.error("PlugIn canceled!");
		// return;
		// }

		String jar = defaultfilename = gd.getNextString();
		String dir = defaultPath = gd.getNextString();
		System.out.println("file: " + jar);
		System.out.println("dir: " + dir);
		GenericDialogPlus workflowView = new GenericDialogPlus("Workflow");

		workflowView.addButton("Send Jar", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send Jar clicked");
				String clusterJar = Config.getClusterPath()
						+ jar.split("/")[jar.split("/").length - 1];
				System.out.println(clusterJar);
				SCP.send(Config.getPseudo(), Config.getHost(), 22, jar, clusterJar);
			}
		});
		workflowView.addButton("Send input files", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send input files clicked");
				String clusterInput = Config.getClusterPath()
						+ dir.split("/")[dir.split("/").length - 1];
				Config.setLocalInputs(new String[] { dir });
				Config.setClusterInputs(new String[] { clusterInput });
				System.out.println(clusterInput);
				SCP.send(Config.getPseudo(), Config.getHost(), 22, dir, clusterInput);
			}
		});
		workflowView.addButton("generate config.sh", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("generate config.sh clicked");
				try {
					String scriptPath = ScriptGenerator.generateScript(jar.split("/")[jar.split("/").length - 1],
							new String[] { dir.split("/")[dir.split("/").length - 1] });
					System.out.println("Script generated: " + scriptPath);
					System.out.println("Sending Script..");
					String clusterScript = scriptPath.split("/")[scriptPath.split("/").length - 1];
					System.out.println(clusterScript);
					Config.setScriptFile(clusterScript);
					Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getLocalInputString()));
					ImageJFunctions.show(image).setTitle("Original");
					SCP.send(Config.getPseudo(), Config.getHost(), 22, scriptPath, clusterScript);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		workflowView.addButton("run config.sh", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("run config.sh clicked");
				SCP.run(Config.getPseudo(), Config.getHost(), 22, Config.getClusterPath(),Config.getsSriptFile());
			}
		});
		workflowView.addButton("get status", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("get status clicked");
			}
		});

		workflowView.addButton("get data", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("get data clicked");
				SCP.get(Config.getPseudo(), Config.getHost(), 22, Config.getClusterInputString(),
						Config.getLocalInputString());
				Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getLocalInputString()));
				ImageJFunctions.show(image).setTitle("Result");
			}
		});

		workflowView.showDialog();
	}

	public static void main(String[] args) {
		new MainFrame_().run("");
	}
}
