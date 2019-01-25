package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlocksManager;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.tools.Config;

public class BlockCombinator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		MyLogger.log.info("Generate result..");
		Config.resultImage = BlocksManager.generateResult(Workflow.blockMap, Config.getTempFolderPath(), callback);
		callback.onSuccess(pos);
	
	}

}
