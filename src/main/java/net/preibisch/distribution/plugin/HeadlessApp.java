package main.java.net.preibisch.distribution.plugin;

import java.io.File;
import java.io.IOException;

import com.jcraft.jsch.JSchException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.clustering.ClusterFile;
import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import main.java.net.preibisch.distribution.algorithm.clustering.scripting.ClusterScript;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.util.Util;

public class HeadlessApp {
	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
	private final static String output_path = "/home/mzouink/Desktop/testn5/output45.n5";

	public static void main(String[] args) throws SpimDataException, IOException, JSchException {

		new ImageJ();
		MyLogger.initLogger();

		new Job();

		// Input XML
		XMLFile inputFile = new XMLFile(input_path);

		// Connection
		Login.login();
		// SessionManager.connect();

		// Generate Metadata
		N5File outputFile = new N5File(output_path, inputFile.getDims());
		System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

		BlocksMetaData md = MetadataGenerator.genarateMetaData(inputFile.bb(), outputFile.getBlocksize());
		File metadataFile = Job.file("metadata.json"); 
		md.toJson(metadataFile);
		
		inputFile.getRelatedFiles().add(metadataFile);

		// Send input with related files
//		SCPManager.sendInput(inputFile);

		ClusterFile clusterFolderName = new ClusterFile(Login.getServer().getPath(), Job.getId());
//		SCPManager.createClusterFolder(clusterFolderName.getPath());
		
		// Generate script
		File scriptFile = Job.file("task.sh") ;
		String metadataCluster = clusterFolderName.file(metadataFile);
		String inputCluster = clusterFolderName.file(inputFile);
		ClusterScript.generateTaskScript(scriptFile, metadataCluster, inputCluster);
		// Generate batch

		// Generate output in server

		// Run

	}
}
