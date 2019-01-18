package main.java.com.clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.java.com.controllers.items.callback.AbstractCallBack;
import main.java.com.tools.Config;

public class BatchGenerator {

	public static void GenerateBatchForLocalFiles(int tasksPerJob, int totalInputFiles,AbstractCallBack  callback,int taskPos) {
		String id = Config.getLogin().getId();
		String path = Config.getLogin().getServer().getPath();
		boolean error = false;
		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath() + "/submit.cmd";
		System.out.println("Input total files:" + totalInputFiles + " - Tasks Per job:" + tasksPerJob);
		int jobs = totalInputFiles / tasksPerJob;
		int restPortions = totalInputFiles % tasksPerJob;
		try (PrintWriter out = new PrintWriter(filePath)) {
			out.println("#!/bin/bash");
			out.println("cd " + path);
			int i = 0;
			for (i = 0; i < jobs; i++) {
				if (tasksPerJob > 1) {
					if (i == 0) {
						out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + "-"
								+ ((i + 1) * tasksPerJob) + " ./task.sh");
					} else {
						out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + "-"
								+ ((i + 1) * tasksPerJob) + " -hold_jid task_" + i + " ./task.sh");
					}
				} else {
					if (i == 0) {
						out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + " ./task.sh");
					} else {
						out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + " -hold_jid task_"
								+ i + " ./task.sh");
					}
				}
				out.println("qsub -N \"prov_" + (i + 1) + "\" -t " + (i + 1) + " -hold_jid task_" + (i + 1)
						+ " -v uuid=" + id + " ./logProvider.sh");
			}
			if (restPortions > 0) {
				if ((i * tasksPerJob + 1) == (i * tasksPerJob + restPortions)) {
					out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + " -hold_jid task_" + i
							+ " ./task.sh");
				} else {
					out.println("qsub -N \"task_" + (i + 1) + "\" -t " + (i * tasksPerJob + 1) + "-"
							+ (i * tasksPerJob + restPortions) + " -hold_jid task_" + i + " ./task.sh");
				}
				out.println("qsub -N \"prov_" + (i + 1) + "\" -t " + (i + 1) + " -hold_jid task_" + (i + 1)
						+ " -v uuid='" + id + "' ./logProvider.sh");
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			e.printStackTrace();
			error = true;
		}
		if (!error) {
			callback.onSuccess(taskPos);
		}
	}
	
	public static void GenerateBatchForClusterFile( AbstractCallBack callback, int taskPos) {
		String id = Config.getLogin().getId();
		String path = Config.getLogin().getServer().getPath();
		boolean error = false;
		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath() + "/submit.cmd";
		try (PrintWriter out = new PrintWriter(filePath)) {
			out.println("#!/bin/bash");
			out.println("cd " + path);
            out.println("qsub -N \"task_1\" ./task.sh");
		out.println("qsub -N \"prov_1\" -hold_jid task_1 -v uuid=" + id + " ./logProvider.sh");
			
			
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			e.printStackTrace();
			error = true;
		}
		if (!error) {
			callback.onSuccess(taskPos);
		}
	}

	public static void main(String[] args) {
		BatchGenerator.GenerateBatchForLocalFiles(10, 94, new AbstractCallBack() {

			@Override
			public void onSuccess(int pos) {
				System.out.println("Done!");

			}

			@Override
			public void onError(String error) {
				System.out.println(error);

			}

			@Override
			public void log(String log) {
				// TODO Auto-generated method stub

			}
		}, 0);
	}
}
