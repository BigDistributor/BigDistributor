package main.java.com.master;

import main.java.com.gui.ProgressGUI;
import main.java.com.tools.Config;

public class ClusterInputMain {
	public static void main(String[] args) {
//		Config.setLocalTaskPath(jarPicker.getSelectedFilePath());	
//		Config.setOriginalInputFilePath(inputPicker.getSelectedFilePath());
//		setVisible(false); 
//		dispose();
		Config.setOriginalInputFilePath("/fast/AG_Preibisch/Marwan/clustering/file.tif");
		Config.init();
		ProgressGUI progressGUI = new ProgressGUI("Progress..");
		progressGUI.setVisible(true);
	}
}
