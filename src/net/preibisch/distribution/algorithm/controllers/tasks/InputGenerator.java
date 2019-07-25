package net.preibisch.distribution.algorithm.controllers.tasks;

import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class InputGenerator implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		System.out.println("TODO ! Input generator");
//		Boolean valid = true;
//		callback.log("Generate input blocks..");
//		Workflow.progressBarPanel.updateBar(0);
//		Workflow.blocks = BlocksManager.generateBlocks(Config.getDataPreview(), callback);
//
//		//TODO create object to send ProcessData
//		Config.setTotalInputFiles(Workflow.blocks.size());
//		try {
//			BlocksManager.saveBlocks(
////				IOFunctions.openAs32Bit(new File(Config.getJob().getInput().getFile().getAll())), 
//					Config.getJob().getInput().getLoader().fuse(),
//					Config.getDataPreview().getBlocksSizes(),
//					Workflow.blocks,
//					new AbstractCallBack() {
//
//				@Override
//				public void onSuccess(int pos) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onError(String error) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void log(String log) {
//					callback.log(log);
//
//				}
//			});
//		} catch (IncompatibleTypeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (valid)
//			callback.onSuccess(pos);

	}

}
