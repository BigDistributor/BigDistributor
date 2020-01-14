package net.preibisch.distribution.algorithm.controllers.tasks;

import java.io.File;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.io.IOTools;

public class ResultManager {
	
	public static void assembleBlockToResult(int key, AbstractCallBack callback) {
		try {
		String string = Job.getTmpDir() + "/" + key + ".tif";
		Img<FloatType> tmp = IOTools.openAs32Bit(new File(string));
//		Workflow.blockMap.get(key).pasteBlock(Config.resultImage, tmp, callback);
		callback.onSuccess(0);
		}catch(Exception e ) {
			callback.onError(e.toString());
			callback.onError("Can't past to result");
		}
	}

}
