package main.java.com.clustering.workflow;

import java.io.IOException;
import java.util.ArrayList;

import com.jcraft.jsch.JSchException;

import main.java.com.blockmanager.BlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.clustering.jsch.SCP;
import main.java.com.tools.Config;
import main.java.com.tools.Helper;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.exception.IncompatibleTypeException;

public class InputManager {
	public static void generateInput(MyCallBack callback) {
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Generate input blocks..");
				Workflow.progressBarPanel.updateBar(0);
				Workflow.blocks = BlocksManager.generateBlocks(Config.getDataPreview(), callback);
				//TODO create object to send ProcessData
				Config.setTotalInputFiles(Workflow.blocks.size());
				try {
					Workflow.blockMap = BlocksManager.saveBlocks(
//						IOFunctions.openAs32Bit(new File(Config.getJob().getInput().getFile().getAll())), 
							Config.getJob().getInput().getLoader().fuse(),
							Config.getDataPreview().getBlocksSizes(),
							Workflow.blocks,
							new MyCallBack() {

						@Override
						public void onSuccess() {
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
					callback.onSuccess();
			}
		});
		task.run();
	}

	public static void sendInput(MyCallBack callBack) {
		Thread task = new Thread(new Runnable() {

			@Override
			public void run() {
				Boolean valid = true;
				IOFunctions.println("Send input files..");
				Workflow.progressBarPanel.updateBar(0);
				String local = Config.getTempFolderPath();
				ArrayList<String> files = Helper.getFiles(local, Config.getInputPrefix());
				Config.setBlocksFilesNames(files);
				int key = 0;
				for (String file : files) {
					try {
						SCP.send(Config.getLogin(), local + "//" + file,
								Config.getLogin().getServer().getPath() + "//" + file, key);
					} catch (JSchException | IOException e) {
						valid = false;
						callBack.onError(e.toString());
					}
					key++;
					Workflow.progressBarPanel.updateBar((key * 100) / files.size());
				}
				if (valid)
					callBack.onSuccess();
			}
		});
		task.run();
	}

	
}
