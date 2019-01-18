package main.java.com.controllers.tasks;

import main.java.com.blockmanager.BlocksManager;
import main.java.com.clustering.workflow.Workflow;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.exception.IncompatibleTypeException;

public class InputGenerator implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {

		Boolean valid = true;
		IOFunctions.println("Generate input blocks..");
		Workflow.progressBarPanel.updateBar(0);
		Workflow.blocks = BlocksManager.generateBlocks(Config.getDataPreview(), callback);
		//TODO create object to send ProcessData
		Config.setTotalInputFiles(Workflow.blocks.size());
		try {
			Workflow.blockMap = BlocksManager.saveBlocks(
//				IOFunctions.openAs32Bit(new File(Config.getJob().getInput().getFile().getAll())), 
					Config.getJob().getInput().getLoader().fuse(),
					Config.getDataPreview().getBlocksSizes(),
					Workflow.blocks,
					new AbstractCallBack() {

				@Override
				public void onSuccess(int pos) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(String error) {
					// TODO Auto-generated method stub

				}

				@Override
				public void log(String log) {
					IOFunctions.println(log);

				}
			});
		} catch (IncompatibleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Config.setBlocks(Workflow.blockMap.size());
		if (valid)
			callback.onSuccess(pos);
	
	}

}
