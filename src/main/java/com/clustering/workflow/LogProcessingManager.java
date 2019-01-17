package main.java.com.clustering.workflow;

import main.java.com.clustering.MyCallBack;
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
				DataGetter.getBlockDataBack((id - 1) * Config.parallelJobs + 1, id * Config.parallelJobs, new MyCallBack() {

					@Override
					public void onSuccess() {
						for (int i = (id - 1) * Config.parallelJobs + 1; i <= id * Config.parallelJobs; i++) {
							ResultManager.assembleBlockToResult(i, new MyCallBack() {

								@Override
								public void onSuccess() {
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
