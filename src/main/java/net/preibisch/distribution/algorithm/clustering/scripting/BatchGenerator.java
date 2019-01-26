package main.java.net.preibisch.distribution.algorithm.clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.Config;

public class BatchGenerator {

	public static final String BATCH_CLUSTER_NAME = "submit.cmd";

	public static final String TASK_JOB_NAME = "task_";
	public static final String PREPROCESS_JOB_NAME = "preprocess_";
	public static final String LOG_JOB_NAME = "provider_";
	
	public static void GenerateBatchForLocalFiles(int tasksPerJob, int totalInputFiles,AbstractCallBack  callback,int taskPos) {
		String id = Config.getLogin().getId();
		String path = Config.getLogin().getServer().getPath();
		boolean error = false;
		File file = new File(Config.getTempFolderPath(),BATCH_CLUSTER_NAME);
		System.out.println("Input total files:" + totalInputFiles + " - Tasks Per job:" + tasksPerJob);
		int jobs = totalInputFiles / tasksPerJob;
		int restPortions = totalInputFiles % tasksPerJob;
		try (PrintWriter out = new PrintWriter(file)) {
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
		File file = new File(Config.getTempFolderPath(),BATCH_CLUSTER_NAME);
		try (PrintWriter out = new PrintWriter(file)) {
			out.println("#!/bin/bash");
			out.println("cd " + path);
            out.println("qsub -N \"task_1\" ./task.sh");
            out.println("qsub -N \"prov_1\" -hold_jid task_1 -v uuid=" + id + " ./logProvider.sh");
    		
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

	
	public static void GenerateBatchForClusterFile( AbstractCallBack callback, int totalBlocks, int taskPos) {
		String path = Config.getLogin().getServer().getPath();
		File file = new File(Config.getTempFolderPath(),BATCH_CLUSTER_NAME);
		try (PrintWriter out = new PrintWriter(file)) {
			out.println("#!/bin/bash");
			File f = new File(path,Config.getLogin().getId());
			out.print("mkdir "+ f.getAbsolutePath());
			out.println("cd " + f.getAbsolutePath());
            for (int i = 1;i<= totalBlocks;i++ ) {
            	out.println(getPreProcessLine(i));	
            	out.println(getTaskLine(i));
            	out.println(getLogProviderLine(i));
            }
            out.flush();
			out.close();
			callback.onSuccess(taskPos);
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			e.printStackTrace();
		}
	}

	private static String getLogProviderLine(int i) {
		String id = Config.getLogin().getId();
		return "qsub -N \"" + LOG_JOB_NAME + i 
				+ " -hold_jid "+ TASK_JOB_NAME + i
				+ " -v uuid=" + id 
				+ " ./" + ShellGenerator.LOG_PROVIDER_SHELL_NAME;
	}

	private static String getPreProcessLine(int i) {
		return "qsub -N \""+ PREPROCESS_JOB_NAME + i 
				+ "\" -t " + i + " ./"+ShellGenerator.PREPROCESS_SHELL_NAME;
	}
	
	private static String getTaskLine(int i) {
		return "qsub -N \""+TASK_JOB_NAME+i 
				+ "\" -t " + i 
				+ " -hold_jid "+ PREPROCESS_JOB_NAME + i
				+ " ./"+ShellGenerator.TASK_SHELL_NAME;
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
