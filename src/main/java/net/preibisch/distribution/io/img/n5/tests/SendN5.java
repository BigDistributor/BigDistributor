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
import net.preibisch.distribution.io.img.XMLFile;

public class SendN5 {

	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";

	private static String path = "/home/mzouink/Desktop/testn5/";
	private static String[] files = new String[] { "back_output45.n5"};

	public static void main(String[] args) throws SpimDataException, IOException, JSchException, SftpException {

		MyLogger.initLogger();
		XMLFile inputFile = XMLFile.XMLFile(input_path);
		for (String s : files)
			inputFile.getRelatedFiles().add(new File(path, s));
		File clusterFolderName = new File(Connection.getServer().getPath(), Job.get().getId());

		SCPManager.sendInput(inputFile, clusterFolderName);
	}

}
