package net.preibisch.bigdistributor.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import net.preibisch.bigdistributor.algorithm.controllers.items.Job;
import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.bigdistributor.algorithm.clustering.jsch.SCPManager;

public class DistributedFile extends File {

	private FileStatus mode = FileStatus.IN_CLUSTER;
	protected List<File> relatedFiles = new ArrayList<>();

	public DistributedFile(String path, FileStatus mode) throws IOException {
		super(path);
		if (mode == FileStatus.IN_LOCAL_COMPUTER)
			if (!exists())
				throw new IOException("File not found! " + path);
		this.mode = mode;
		relatedFiles.add(this);
	}

	public void prepare() throws JSchException, IOException, SftpException {
		if (mode == FileStatus.IN_LOCAL_COMPUTER) {
			SCPManager.send(relatedFiles, Job.get().getCluster());
		}
	}

	public String getClusterPath() throws JSchException, SftpException {
		switch (mode) {
		case IN_LOCAL_COMPUTER:
			return new File(Job.get().getCluster().getAbsolutePath(), this.getName()).getAbsolutePath();
		case IN_CLUSTER:
			return this.getAbsolutePath();

		default:
			MyLogger.log().error("Invalide mode to get task cluster path ");
			return this.getAbsolutePath();
		}
	}
}
