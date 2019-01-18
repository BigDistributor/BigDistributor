package main.java.com.clustering.workflow;

import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.controllers.tasks.DataGetter;
import main.java.com.controllers.tasks.ResultManager;
import main.java.com.gui.items.Colors;
import main.java.com.tools.Config;
import mpicbg.spim.io.IOFunctions;

public class LogProcessingManager {
	
	public static void processClusterLog(String log) {
		String[] parts = log.split(";");
		try {
//			System.out.println("ProcessLog:"+ parts[1] +" - " + Config.getUUID()+" "+(parts[1] == Config.getUUID())+" "+parts[1].equals(Config.getUUID()));
			if (parts[1].equals(Config.getLogin().getId()) ) {
				IOFunctions.println("Log got:" + log);
				int id = Integer.parseInt(parts[2]);

				for (int j = (id - 1) * Config.parallelJobs; j < id * Config.parallelJobs; j++) {
					try {
						Config.getDataPreview().getBlocksPreview().get(j).setStatus(Colors.PROCESSED);

					} catch (IndexOutOfBoundsException e) {
						System.out.println("Error! Invalide elmn");
					}
				}
				DataGetter.getBlockDataBack((id - 1) * Config.parallelJobs + 1, id * Config.parallelJobs, new AbstractCallBack() {

					@Override
					public void onSuccess(int pos) {
						for (int i = (id - 1) * Config.parallelJobs + 1; i <= id * Config.parallelJobs; i++) {
							ResultManager.assembleBlockToResult(i, new AbstractCallBack() {

								@Override
								public void onSuccess(int pos) {
//									ImageJFunctions.show(Config.resultImage).setTitle("Result");

								}

								@Override
								public void onError(String error) {
									// TODO Auto-generated method stub

								}

								@Override
								public void log(String log) {
									// TODO Auto-generated method stub

								}
							});

						}
					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void log(String log) {
						// TODO Auto-generated method stub

					}
				});

			}
		} catch (NumberFormatException e) {
			System.out.println("Error! converting log error");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error! Invalide log");
		}
	}

}
