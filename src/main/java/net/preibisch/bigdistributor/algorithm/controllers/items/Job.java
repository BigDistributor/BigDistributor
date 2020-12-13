package net.preibisch.bigdistributor.algorithm.controllers.items;

import com.google.common.io.Files;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import net.preibisch.bigdistributor.algorithm.clustering.jsch.SCPManager;
import net.preibisch.bigdistributor.algorithm.clustering.server.Connection;
import net.preibisch.bigdistributor.tools.helpers.IDManager;

import java.io.File;

public class Job extends Object {

	public enum DataAccessMode {
		READY_IN_CLUSTER_INPUT, SEND_DATA_FROM_LOCAL
	}

	public enum ProcessMode {
		AWS_PROCESSING, CLUSTER_PROCESSING, LOCAL_PROCESSING
	}

	public static final String TASK_CLUSTER_NAME = "task.jar";

	private String id;
	private DataAccessMode dataAccess;
	private ProcessMode processMode;
	private File tmpDir;
	private File cluster;
	private int totalbBlocks;

	private static Job instance;

	public static Job get() throws JSchException, SftpException {
		if (instance == null) {
			instance = job();
			return get();
		} else {
			return instance;
		}
	}

	public static Job create(ProcessMode processMode) throws JSchException, SftpException {
		instance = job(processMode);
		return get();
	}

	private static Job job(ProcessMode processMode) throws JSchException, SftpException {
		String id = new IDManager(processMode).get();
		System.out.println("Job id: " + id);

		File tmpDir = createTempDir();

		if (processMode == ProcessMode.CLUSTER_PROCESSING)
			return clusterJob(id, tmpDir);
		else if (processMode == ProcessMode.LOCAL_PROCESSING)
			return localJob(id, tmpDir);
		else
			return clusterJob(id, tmpDir);

	}

	private static Job localJob(String id, File tmpDir) {

		return new Job(id, DataAccessMode.READY_IN_CLUSTER_INPUT, ProcessMode.LOCAL_PROCESSING, tmpDir, null, 0);

	}

	private static Job clusterJob(String id, File tmpDir) throws JSchException, SftpException {
		if (!Connection.isConfigured()) {
			Connection.login();
		}
		String clusterPath = Connection.getServer().getPath();
		File cluster = new File(clusterPath, id);

		SCPManager.createClusterFolder(cluster);

		return new Job(id, DataAccessMode.SEND_DATA_FROM_LOCAL, ProcessMode.CLUSTER_PROCESSING, tmpDir, cluster, 0);

	}

	public static Job create() throws JSchException, SftpException {
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

	private static Job job() throws JSchException, SftpException {
		return job(ProcessMode.CLUSTER_PROCESSING);
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
		return new File(cluster.getAbsolutePath(), file).getAbsolutePath();
	}

	public ProcessMode getProcessMode() {
		return processMode;
	}

	public DataAccessMode getDataAccess() {
		return dataAccess;
	}
}
