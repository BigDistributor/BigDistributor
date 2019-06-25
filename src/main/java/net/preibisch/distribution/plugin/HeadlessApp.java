package main.java.net.preibisch.distribution.plugin;

import java.io.File;
import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.clustering.ClusterFile;
import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import main.java.net.preibisch.distribution.algorithm.clustering.scripting.BatchScriptFile;
import main.java.net.preibisch.distribution.algorithm.clustering.scripting.ClusterScript;
import main.java.net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import main.java.net.preibisch.distribution.io.GsonIO;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.util.Util;

public class HeadlessApp {
	private static final String BATCH_NAME = "submit.cmd";
	private static final String TASK_SHELL_NAME = "task.sh";

	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
//	private final static String task_path = "/home/mzouink/Desktop/Task/FusionTask2.jar";
	private final static String task_path = "/home/mzouink/Desktop/Task/FusionTask_FullFile.jar";
	
	

	public static void main(String[] args) throws SpimDataException, IOException, JSchException, SftpException {

		new ImageJ();
		MyLogger.initLogger();

		new Job();

		// Input XML
		XMLFile inputFile = XMLFile.XMLFile(input_path);

		// Connection
		Login.login();
		// SessionManager.connect();

		ClusterFile clusterFolderName = new ClusterFile(Login.getServer().getPath(), Job.getId());
		// Generate Metadata
		N5File outputFile = new N5File(Job.file("output.n5").getAbsolutePath(), inputFile.getDims());
		System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

		BlocksMetaData md = MetadataGenerator.genarateMetaData(inputFile.bb(), outputFile.getBlocksize());
		File metadataFile = Job.file("metadata.json");
		Job.setTotalbBlocks(md.getTotal());
//		md.toJson(metadataFile);
		GsonIO.toJson(md, metadataFile);

		// create output
//		outputFile.create();

		// Create project folder in the cluster
//		SCPManager.createClusterFolder(clusterFolderName);

//		inputFile.getRelatedFiles().add(outputFile);

		// Generate script
		File scriptFile = Job.file(TASK_SHELL_NAME);
		File metadataCluster = clusterFolderName.subfile(metadataFile);
		File inputCluster = clusterFolderName.subfile(inputFile);
		File clusterOutput = clusterFolderName.subfile(outputFile);

		File taskFile = new File(task_path);
		ClusterScript.generateTaskScript(scriptFile, taskFile.getName(), metadataCluster.getPath(),
				inputCluster.getPath(), clusterOutput.getPath());

		
		// Task to prepare N5
		File prepareShell = Job.file(TaskType.file(TaskType.PREPARE));
		ClusterScript.generateTaskScript(TaskType.PREPARE, prepareShell, taskFile.getName(), metadataCluster.getPath(),
						inputCluster.getPath(), clusterOutput.getPath(), "");
				
		// Generate batch
		File batchScriptFile = Job.file(BATCH_NAME);
		BatchScriptFile.generate(batchScriptFile, clusterFolderName.getPath(), md.getTotal()); //md.getTotal()


		// send all
		inputFile.getRelatedFiles().add(metadataFile);
		inputFile.getRelatedFiles().add(batchScriptFile);
		inputFile.getRelatedFiles().add(scriptFile);
		

		inputFile.getRelatedFiles().add(prepareShell);

		inputFile.getRelatedFiles().add(taskFile);
		SCPManager.sendInput(inputFile, clusterFolderName);

		// Run
		SCPManager.startBatch(clusterFolderName.subfile(batchScriptFile));
	}
}
