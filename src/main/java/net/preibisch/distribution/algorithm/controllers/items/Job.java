package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import main.java.net.preibisch.distribution.io.img.ImgFile;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import main.java.net.preibisch.distribution.tools.Tools;
import mpicbg.spim.data.SpimDataException;

public class Job extends Object {

	public static final String TASK_CLUSTER_NAME = "task.jar";

	private static String id;
	private static TaskFile task;
	private static AbstractTaskParams extra;
	private static int totalBlocks;
	private static XMLFile input;
	private static N5File output;
	private static AppMode appMode;
	private static String tmpDir;
	private static String metaDataPath;
	private static String clusterShellFilePath;
	private static String batchFilePath;

	private Job(TaskFile task, AbstractTaskParams extra, XMLFile input, N5File output, AppMode appMode, String tmpDir,
			String metaDataPath, String taskShellPath) {
		Job.id = Tools.id();
		Job.task = task;
		Job.extra = extra;
		Job.input = input;
		Job.output = output;
		Job.appMode = appMode;
		Job.tmpDir = tmpDir;
		Job.metaDataPath = metaDataPath;
		Job.taskShellPath = taskShellPath;
	}

	public static void initJob(AppMode mode, String taskpath, String input) throws SpimDataException, IOException {

		XMLFile inputData = new XMLFile(input);
		N5File outputData;
		String tmpDir = createTempDir();
		
//		this(null,null,inputData,outputData,mode,tmpDir,metadataPath,clusterShellFilePath,batchFilePath);
	}
	
	public static void initJob(String task, String input) throws SpimDataException, IOException{
		initJob(AppMode.CLUSTER_INPUT_MODE,task,input);
	}
	
	
	public static String createTempDir() {
		File tempDir = Files.createTempDir();
		return tempDir.getAbsolutePath();
	}
	
	public static int getTotalBlocks() {
		return totalBlocks;
	}

	public static ImgFile getOutput() {
		return output;
	}
	public static String getId() {
		return id;
	}

	public static TaskFile getTask() {
		return task;
	}

	public static ImgFile getInput() {
		return input;
	}

	public static AbstractTaskParams getExtra() {
		return extra;
	}

	public static AppMode getAppMode() {
		return appMode;
	}

	public static String getTmpDir() {
		return tmpDir;
	}

	public static String getMetaDataPath() {
		return metaDataPath;
	}

	public static void setMetaDataPath(String metaDataPath) {
		Job.metaDataPath = metaDataPath;
	}

	public static void setTaskShellPath(String taskShellPath) {
		Job.taskShellPath = taskShellPath;

	}

	public static String getTaskShellPath() {
		return taskShellPath;
	}

	
}
