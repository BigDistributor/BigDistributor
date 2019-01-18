package main.java.com.clustering.workflow;

import java.io.File;

import main.java.com.blockmanager.BlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class ResultManager {
	public static void combineData(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				IOFunctions.println("Generate result..");
				Config.resultImage = BlocksManager.generateResult(Workflow.blockMap, Config.getTempFolderPath(), callBack);
				callBack.onSuccess();
			}
		});
		task.run();

	}

	
	public static void assembleBlockToResult(int key, MyCallBack callback) {
		try {
		String string = Config.getTempFolderPath() + "/" + key + ".tif";
		Img<FloatType> tmp = IOFunctions.openAs32Bit(new File(string));
		Workflow.blockMap.get(key).pasteBlock(Config.resultImage, tmp, callback);
		callback.onSuccess();
		}catch(Exception e ) {
			System.out.println(e.toString());
			System.out.println("Can't past to result");
		}
	}

}
