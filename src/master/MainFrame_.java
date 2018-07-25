package master;

import gui.InputFrame;
import ij.plugin.PlugIn;

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
