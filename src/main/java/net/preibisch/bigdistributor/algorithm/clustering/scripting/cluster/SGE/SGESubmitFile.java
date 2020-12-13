package net.preibisch.bigdistributor.algorithm.clustering.scripting.cluster.SGE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.bigdistributor.algorithm.clustering.scripting.JobType;

public class SGESubmitFile {

	public static final String TASK_JOB_NAME = "task_";
	public static final String PREPROCESS_JOB_NAME = "preprocess_";
	
	public static void generate(File file,String clusterPath,  int total) throws FileNotFoundException {
		generate(file, clusterPath, total, 0, JobType.file(JobType.PREPARE), JobType.file(JobType.PROCESS));
	}

	public static void generate(File file, String clusterPath, int total, int index, String prepareScriptName,
			String taskScriptName) throws FileNotFoundException {

		MyLogger.log().info("Start Generate batch file.. ");

		System.out.println("Input total files:" + total);
		try(PrintWriter out = new PrintWriter(file)){
			out.println("#!/bin/bash");
			out.println("cd " + clusterPath);
			String prepName = "prep_"+index;
			out.println("qsub -N \""+prepName+"\" -t " + 1 + " ./"+prepareScriptName);

			for (int i = 1; i <= total; i++) {

				String taskName = "task_"+index+"_" + i ;
				String hold = "";
				hold = " -hold_jid \""+prepName+"\"";
				out.println("qsub -N \""+taskName+"\""+hold+" -t " + i + " ./"+taskScriptName);
			}
			out.flush();
			out.close();
		}
	
	}
}
