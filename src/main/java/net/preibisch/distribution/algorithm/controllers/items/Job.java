package net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;

import com.google.common.io.Files;

import net.preibisch.distribution.algorithm.clustering.server.Connection;
import net.preibisch.distribution.tools.helpers.JobHelpers;

public class Job extends Object {
	
	public enum DataAccessMode {
		READY_IN_CLUSTER_INPUT,
		SEND_DATA_FROM_LOCAL
	}

	public enum ProcessMode{
		CLUSTER_PROCESSING,
		LOCAL_PROCESSING
	}

	public static final String TASK_CLUSTER_NAME = "task.jar";

	private String id;
	private DataAccessMode dataAccess;
	private ProcessMode processMode;
	private File tmpDir;
	private File cluster;
	private int totalbBlocks;
	
	private static Job instance;
	
	public static Job get() {
		if (instance==null) {
			instance = job();
			return get();
		}else {
			return instance;
		}
	}
	
	public static Job create() {
		instance = job();
		return get();
	}
	

	private Job(String id, DataAccessMode dataAccess, ProcessMode processMode, File tmpDir, File cluster,
			int totalbBlocks) {
		super();
		this.id = id;
		this.dataAccess = dataAccess;
		this.processMode = processMode;
		this.tmpDir = tmpDir;
		this.cluster = cluster;
		this.totalbBlocks = totalbBlocks;
	}


	private static Job job() {

		String id = JobHelpers.id();
		System.out.println("Job id: "+id);

		File tmpDir = createTempDir();
		if(!Connection.isConfigured()) {
			Connection.login();
		}
		String clusterPath = Connection.getServer().getPath();
		File cluster = new File(clusterPath , id);
		
	return new Job(id,DataAccessMode.SEND_DATA_FROM_LOCAL,ProcessMode.CLUSTER_PROCESSING,tmpDir,cluster,0);
	}

	private static File createTempDir() {
		File tempDir = Files.createTempDir();
		System.out.println("tmp Dir: " + tempDir.getAbsolutePath());
		return tempDir;
	}

	public String getId() {
		return id;
	}

	
	public File getTmpDir() {
		return tmpDir;
	}

	public File file(String string) {
		return new File(tmpDir, string);
	}

	public int getTotalbBlocks() {
		return totalbBlocks;
	}

	public void setTotalbBlocks(int totalbBlocks) {
		this.totalbBlocks = totalbBlocks;
	}

	public File getCluster() {
		return cluster;
	}
	
	public String subfile(String file) {
		return new File(cluster.getAbsolutePath(),file).getAbsolutePath();
	}
	
	public ProcessMode getProcessMode() {
		return processMode;
	}
	
	public DataAccessMode getDataAccess() {
		return dataAccess;
	}
}
