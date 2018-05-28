package gui;

import java.util.ArrayList;

import ij.plugin.PlugIn;
import tools.Config;

public class MainFrame_ implements PlugIn {

	public static String defaultfilename = "";
	public static String defaultPath = "";

	public void run(String arg) {
		// create a dialog with two numeric input fields
		InputFrame inputFrame = new InputFrame("Input");
		Config.log = new ArrayList<String>();
		inputFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame_().run("");
	}
}
