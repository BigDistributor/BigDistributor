package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;

public class BlockCombinator implements AbstractTask{

	@Override
	public void start(int pos, AbstractCallBack callback) {

		MyLogger.log.info("Generate result..");
//		Config.resultImage = BlocksManager.generateResult(Workflow.blockMap, Config.getTempFolderPath(), callback);
		callback.onSuccess(pos);
	
	}

}
