package net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;

import com.google.common.io.Files;

import net.preibisch.distribution.algorithm.controllers.items.server.Login;
import net.preibisch.distribution.tools.Tools;

public class Job extends Object {

	public static final String TASK_CLUSTER_NAME = "task.jar";

	private static String id;
	private static AppMode appMode;
	private static File tmpDir;
	private static File cluster;
	private static int totalbBlocks;

	private Job(String id, AppMode appMode, File tmpDir, File cluster) {
		Job.id = id;
		Job.appMode = appMode;
		Job.tmpDir = tmpDir;
		Job.cluster = cluster;
	}

	public Job(AppMode mode) {

		String id = Tools.id();
		System.out.println("Job id: "+id);

		File tmpDir = createTempDir();
		if(!Login.isConfigured()) {
			Login.login();
		}
		String clusterPath = Login.getServer().getPath();
		File cluster = new File(clusterPath , id);
		new Job(id, mode, tmpDir, cluster);
	}

	public Job() {
		new Job(AppMode.CLUSTER_INPUT_MODE);
	}

	public static File createTempDir() {
		File tempDir = Files.createTempDir();
		System.out.println("tmp Dir: " + tempDir.getAbsolutePath());
		return tempDir;
	}

	public static String getId() {
		return id;
	}

	public static AppMode getAppMode() {
		return appMode;
	}

	public static File getTmpDir() {
		return tmpDir;
	}

	public static File file(String string) {
		return new File(tmpDir, string);
	}

	public static int getTotalbBlocks() {
		return totalbBlocks;
	}

	public static void setTotalbBlocks(int totalbBlocks) {
		Job.totalbBlocks = totalbBlocks;
	}

	public static File getCluster() {
		return cluster;
	}
}
