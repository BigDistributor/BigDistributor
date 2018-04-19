package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiji.util.gui.GenericDialogPlus;
import ij.plugin.PlugIn;
import tools.Config;

public class MainFrame_ implements PlugIn {

	public static String defaultfilename = "";
	public static String defaultPath = "";

	public void run(String arg) {
		// create a dialog with two numeric input fields
		GenericDialogPlus gd = new GenericDialogPlus("MDC Clustering");
	
		gd.addFileField( "Jar File", defaultfilename, 65 );
		gd.addDirectoryField("Dir", defaultPath, 65 );
		
		gd.addButton("Config", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GenericDialogPlus config = new GenericDialogPlus("Cluster config");
				config.addStringField("Host", "localhost");
				config.addNumericField("Port", 22, 5);
				config.addStringField("Pseudo", "Marwan");
				config.addStringField("PW", "");
				config.showDialog();
				Config.init(config.getNextString(),(int)config.getNextNumber(),config.getNextString(),config.getNextString());
			}
		});

		gd.showDialog();
//		if (gd.wasCanceled()) {
//			IJ.error("PlugIn canceled!");
//			return;
//		}
		
		String jar = defaultfilename = gd.getNextString();
		String dir = defaultPath = gd.getNextString();
		System.out.println("file: "+jar);
		System.out.println("dir: "+dir);
		GenericDialogPlus workflowView = new GenericDialogPlus("Workflow");
		workflowView.addButton("Send Jar", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send Jar clicked");
			}
		});
		workflowView.addButton("Send input files", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Send input files clicked");
			}
		});
		workflowView.addButton("generate config.sh", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("generate config.sh clicked");
			}
		});
		workflowView.addButton("run config.sh", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("run config.sh clicked");
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
			}
		});
		
		workflowView.showDialog();
	}

}

