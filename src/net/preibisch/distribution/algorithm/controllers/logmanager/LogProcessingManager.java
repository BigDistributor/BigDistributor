package net.preibisch.distribution.algorithm.controllers.logmanager;

import mpicbg.spim.io.IOFunctions;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.tasks.DataGetter;
import net.preibisch.distribution.algorithm.controllers.tasks.ResultManager;
import net.preibisch.distribution.gui.items.Colors;
import net.preibisch.distribution.gui.items.DataPreview;
import net.preibisch.distribution.tools.config.DEFAULT;

public class LogProcessingManager {
	
	public static void processClusterLog(String log) {
		String[] parts = log.split(";");
		int parallelJobs = DEFAULT.PARRALEL_JOB;
		try {
//			System.out.println("ProcessLog:"+ parts[1] +" - " + Config.getUUID()+" "+(parts[1] == Config.getUUID())+" "+parts[1].equals(Config.getUUID()));
			if (parts[1].equals(Job.getId()) ) {
				IOFunctions.println("Log got:" + log);
				int id = Integer.parseInt(parts[2]);

				for (int j = (id - 1) * parallelJobs; j < id * parallelJobs; j++) {
					try {
						DataPreview.getBlocksPreview().get(j).setStatus(Colors.PROCESSED);

					} catch (IndexOutOfBoundsException e) {
						System.out.println("Error! Invalide elmn");
					}
				}
				DataGetter.getBlockDataBack((id - 1) * parallelJobs + 1, id * parallelJobs, new AbstractCallBack() {

					@Override
					public void onSuccess(int pos) {
						for (int i = (id - 1) * parallelJobs + 1; i <= id * parallelJobs; i++) {
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
