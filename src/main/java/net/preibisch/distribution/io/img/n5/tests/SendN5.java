package net.preibisch.distribution.io.img.n5.tests;

import java.io.File;
import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import net.preibisch.distribution.algorithm.clustering.server.Connection;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.io.img.xml.XMLFile;

public class SendN5 {

	private final static String path = "/home/mzouink/Desktop/testn5/back_output45.n5";

	public static void main(String[] args) throws SpimDataException, IOException, JSchException, SftpException {


		File clusterFolderName = new File(Connection.getServer().getPath(), Job.get().getId());

		SCPManager.sendFolder(path, clusterFolderName.getAbsolutePath());
	}

}
