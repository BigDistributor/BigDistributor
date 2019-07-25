package net.preibisch.distribution.algorithm.clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class ClusterScript {
	public static final String TASK_SHELL_NAME = "task.sh";

	public static final String PREPROCESS_SHELL_NAME = "preprocess.sh";
	public static final String LOG_PROVIDER_SHELL_NAME = "logProvider.sh";

	public static void generateTaskScript(File file, String metadata, String input, String output) throws IOException {
		generateTaskScript(file, TASK_SHELL_NAME, metadata, input, output);
	}

	public static void generateTaskScript(File file, String taskName, String metadata, String input, String output)
			throws IOException {
		generateTaskScript(TaskType.PROCESS, file, taskName, metadata, input, output, "");
	}

	public static void generateTaskScript(TaskType type, File file, String taskName, String metadata, String input,
			String output, String extraParams) throws IOException {
		System.out.println("Create Script file: " + file.getAbsolutePath());
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
			out.println(taskLine(type, taskName, metadata, input, output, extraParams));
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			throw new IOException();
		}

		System.out.println("Script file created: " + file.getAbsolutePath());
	}

	private static String taskLine(TaskType type, String taskName, String metadata, String input, String output,
			String extraParams) {
		return "java -jar " + taskName + " -t " +TaskType.str(type)+ " -i " + input + " -o " + output + " -m " + metadata
				+ " -id $SGE_TASK_ID";
	}

	// provider.sh
	public static String generateLogProviderShell(int pos, AbstractCallBack callback) {

		String filePath = Job.getTmpDir().getAbsolutePath() + "/logProvider.sh";

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
