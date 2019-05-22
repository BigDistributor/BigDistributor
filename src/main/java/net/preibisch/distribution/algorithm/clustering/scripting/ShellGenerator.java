package main.java.net.preibisch.distribution.algorithm.clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class ShellGenerator {
	public static final String TASK_SHELL_NAME = "task.sh";

	public static final String PREPROCESS_SHELL_NAME = "preprocess.sh";
	public static final String LOG_PROVIDER_SHELL_NAME = "logProvider.sh";
	
	
	public static void generateTaskShell(int pos,AbstractCallBack callback)  {

		String tmpDir = Job.getTmpDir();
		File file = new File(tmpDir ,TASK_SHELL_NAME);
		
		try (PrintWriter out = new PrintWriter(file)) {
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
			out.println(getTaskLine());
			out.flush();
			out.close();
			Job.setTaskShellPath(file.getAbsolutePath());
			callback.onSuccess(pos);
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
		}
	}

	private static String getTaskLine() {
		String taskLine  = "";
		if(AppMode.LOCAL_INPUT_MODE.equals(Job.getAppMode())) {
			taskLine = getLocalInputTaskLine();
		}
			else if (AppMode.CLUSTER_INPUT_MODE.equals(Job.getAppMode())) {
				taskLine = getCloudInputTaskLine();
			}
		return taskLine;
	}

	private static String getCloudInputTaskLine() {
		return "java -jar task.jar"
				+" -i " + Job.getInput()
				+" -x $SGE_TASK_ID" ;
	}

	private static String getLocalInputTaskLine() {
		return "java -jar "+Job.TASK_CLUSTER_NAME+" $SGE_TASK_ID" + ".tif";
		
	}

	// provider.sh
	public static String generateLogProviderShell(int pos, AbstractCallBack callback) {

		File file = new File(Job.getTmpDir());
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
			callback.onSuccess(pos);
			return filePath;
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			return null;
		}
	}
}
