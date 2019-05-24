package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;

import com.google.common.io.Files;

import main.java.net.preibisch.distribution.tools.Tools;

public class Job extends Object {

	public static final String TASK_CLUSTER_NAME = "task.jar";

	private static String id;
	private static AppMode appMode;
	private static String tmpDir;

	private Job(String id, AppMode appMode, String tmpDir) {
		Job.id = id;
		Job.appMode = appMode;
		Job.tmpDir = tmpDir;
	}

	public Job(AppMode mode) {

		String id = Tools.id();

		String tmpDir = createTempDir();

		new Job(id, mode, tmpDir);
	}

	public Job() {
		new Job(AppMode.CLUSTER_INPUT_MODE);
	}

	public static String createTempDir() {
		File tempDir = Files.createTempDir();
		return tempDir.getAbsolutePath();
	}

	public static String getId() {
		return id;
	}

	public static AppMode getAppMode() {
		return appMode;
	}

	public static String getTmpDir() {
		return tmpDir;
	}

}
