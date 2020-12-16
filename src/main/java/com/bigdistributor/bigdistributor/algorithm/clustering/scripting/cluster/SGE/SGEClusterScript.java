package net.preibisch.bigdistributor.algorithm.clustering.scripting.cluster.SGE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import net.preibisch.bigdistributor.algorithm.clustering.scripting.JobType;

public class SGEClusterScript {
	public static final String TASK_SHELL_NAME = "task.sh";

	public static final String PREPROCESS_SHELL_NAME = "preprocess.sh";
	public static final String LOG_PROVIDER_SHELL_NAME = "logProvider.sh";

//	public static void generateTaskScript(File file, String metadata, String input, String output) throws IOException {
//		generateTaskScript(file, TASK_SHELL_NAME, metadata, input, output);
//	}
//
//	public static void generateTaskScript(File file, String taskName, String metadata, String input, String output)
//			throws IOException {
//		generateTaskScript(JobType.PROCESS, file, taskName, metadata, input, output, "");
//	}
//
//	public static void generateTaskScript(File file, String taskName, String metadata, String input, String output,String extraParams)
//			throws IOException {
//		System.out.println(extraParams);
//		generateTaskScript(JobType.PROCESS, file, taskName, metadata, input, output, extraParams);
//	}

	public static void generateTaskScript(JobType type, File file, String taskPath, String metadata, String input,
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
			System.out.println(extraParams);
			out.println(taskLine(type, taskPath, metadata, input, output, extraParams));
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			throw new IOException();
		}

		System.out.println("Script file created: " + file.getAbsolutePath());
	}

	private static String taskLine(JobType type, String taskName, String metadata, String input, String output,
			String extraParams) {
		System.out.println(extraParams);
		String extra = "";
		if (!extraParams.equals("")) {
			extra =" -p "+ extraParams ;
			}
		System.out.println("EXTRA: "+extra+ " | extraParams: "+extraParams);
		return "java -jar " + taskName + " -t " +JobType.str(type)+ " -i " + input + " -o " + output + " -m " + metadata
				+ extra
				+ " -id $SGE_TASK_ID";
	}

}
