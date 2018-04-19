package Helpers;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

public class FrameDemo_ implements PlugIn {



	public void run(String arg) {
		// create a dialog with two numeric input fields
		GenericDialog gd = new GenericDialog("FrameDemo settings");
	
		gd.addNumericField("Frame width:",200.0,3);
		gd.addNumericField("Frame height:",200.0,3);
	
		// show the dialog and quit, if the user clicks "cancel"
		gd.showDialog();
		if (gd.wasCanceled()) {
			IJ.error("PlugIn canceled!");
			return;
		}
	

	}

}
