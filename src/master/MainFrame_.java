package master;

import java.util.ArrayList;

import gui.InputFrame;
import ij.plugin.PlugIn;
import tools.Config;

public class MainFrame_ implements PlugIn {

	public static String defaultfilename = "";
	public static String defaultPath = "";

	public void run(String arg) {
		InputFrame inputFrame = new InputFrame("Input");
		Config.log = new ArrayList<String>();
		inputFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame_().run("");
	}
}
