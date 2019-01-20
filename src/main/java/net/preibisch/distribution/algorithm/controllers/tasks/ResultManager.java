package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.File;

import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class ResultManager {
	
	public static void assembleBlockToResult(int key, AbstractCallBack callback) {
		try {
		String string = Config.getTempFolderPath() + "/" + key + ".tif";
		Img<FloatType> tmp = IOFunctions.openAs32Bit(new File(string));
		Workflow.blockMap.get(key).pasteBlock(Config.resultImage, tmp, callback);
		callback.onSuccess(0);
		}catch(Exception e ) {
			System.out.println(e.toString());
			System.out.println("Can't past to result");
		}
	}

}
