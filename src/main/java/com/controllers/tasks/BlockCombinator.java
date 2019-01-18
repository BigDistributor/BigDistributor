package main.java.com.controllers.tasks;

import main.java.com.blockmanager.BlocksManager;
import main.java.com.clustering.workflow.Workflow;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class BlockCombinator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		IOFunctions.println("Generate result..");
		Config.resultImage = BlocksManager.generateResult(Workflow.blockMap, Config.getTempFolderPath(), callback);
		callback.onSuccess(pos);
	
	}

}
