package main.java.com.clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.java.com.clustering.MyCallBack;
import main.java.com.controllers.items.AppMode;
import main.java.com.tools.Config;

public class ShellGenerator {

//	public static String generateShell(String jar, String[] input, int key) throws FileNotFoundException {
//
//		File file = new File(Config.getTempFolderPath());
//		String filePath = file.getAbsolutePath() + "/run" + key + ".sh";
//
//		try (PrintWriter out = new PrintWriter(filePath)) {
//			out.println("#!/bin/sh");
//			out.println("# This is my job script with qsub-options ");
//			out.println("#$ -pe smp 8");
//			out.println("##$ -pe orte 32");
//			out.println("#$ -V -N \"Task " + key + " runner\"");
//			out.println("#$ -l h_rt=0:0:30 -l h_vmem=4G -l h_stack=128M -cwd");
//			out.println("#$ -o data/test_results-$JOB_ID.txt");
//			out.println("#$ -e data/test_results-$JOB_ID.txt");
//			out.println("# export NSLOTS=8");
//			out.println("# neccessary to prevent python error ");
//			out.println("#export OPENBLAS_NUM_THREADS=4");
//			out.println("# export NUM_THREADS=8");
//			out.println("java -jar " + jar + " " + String.join(" ", input));
//			return filePath;
//		}
//	}

	// job.sh
	public static String generateTaskShell(MyCallBack callback)  {

		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath() + "/task.sh";
	
		try (PrintWriter out = new PrintWriter(filePath)) {
			out.println("#!/bin/sh");
			out.println("# This is my job script with qsub-options ");
			out.println("#$ -pe smp 8");
			out.println("##$ -pe orte 32");
			out.println("#$ -l h_rt=0:0:30 -l h_vmem=4G -l h_stack=128M -cwd");
			out.println("#$ -o output/task-$JOB_ID.txt");
			out.println("#$ -e error/task-$JOB_ID.txt");
			out.println("# export NSLOTS=8");
			out.println("# neccessary to prevent python error ");
			out.println("#export OPENBLAS_NUM_THREADS=4");
			out.println("# export NUM_THREADS=8");
			if(Config.APP_MODE == AppMode.LocalInputMode) {
			out.println("java -jar task.jar $SGE_TASK_ID" + Config.getInputPrefix());}
			else if (Config.APP_MODE == AppMode.ClusterInputMode) {
				out.println("java -jar task.jar " + Config.getJob().getInput().getFile().getAll());
			}
			out.flush();
			out.close();
			callback.onSuccess();
			return filePath;
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			return null;
		}
	}

	// provider.sh
	public static String generateLogProviderShell(MyCallBack callback) {

		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath() + "/logProvider.sh";

		try (PrintWriter out = new PrintWriter(filePath)) {
			out.println("#!/bin/sh");
			out.println("# This is my job script with qsub-options ");
			out.println("#$ -pe smp 8");
			out.println("##$ -pe orte 32");
			out.println("#$ -l h_rt=0:0:30 -l h_vmem=4G -l h_stack=128M -cwd");
			out.println("#$ -o output/log-$JOB_ID.txt");
			out.println("#$ -e error/log-$JOB_ID.txt");
			out.println("# export NSLOTS=8");
			out.println("# neccessary to prevent python error ");
			out.println("#export OPENBLAS_NUM_THREADS=4");
			out.println("# export NUM_THREADS=8");
			out.println("java -jar logProvider.jar ${uuid} $SGE_TASK_ID");
			out.flush();
			out.close();
			callback.onSuccess();
			return filePath;
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			return null;
		}
	}

	public static void main(String[] args) {
		generateLogProviderShell(new MyCallBack() {

			@Override
			public void onSuccess() {
				System.out.println("Done !");

			}

			@Override
			public void onError(String error) {
				System.out.println("Error: " + error);

			}

			@Override
			public void log(String log) {
				// TODO Auto-generated method stub

			}
		});
	}
}
