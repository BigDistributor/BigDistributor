package main.java.com.master;

import ij.plugin.PlugIn;
import main.java.com.gui.InputFrame;

public class MainFrame_ implements PlugIn {

	public static String defaultfilename = "";
	public static String defaultPath = "";

	public void run(String arg) {
		InputFrame inputFrame = new InputFrame("Input");
		inputFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame_().run("");
	}
}
