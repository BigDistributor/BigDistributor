package main.java.net.preibisch.distribution.headless;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
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
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class Clustering {
	private static final String BATCH_NAME = "submit.cmd";
	private static final String TASK_SHELL_NAME = "task.sh";

	private final static String task_path = "/Users/Marwan/Desktop/Fusion.jar";

	public static void main(String[] args) throws SpimDataException, IOException, JSchException, SftpException {
		run("/Users/Marwan/Desktop/grid-3d-stitched-h5/dataset.xml");
	}

	public static void run(String input_path) throws SpimDataException, IOException, JSchException, SftpException {
		run(input_path, "output.n5");
	}

	public static void run(Interval interval, SpimData2 spimdata, double downsampling,
			List<ViewId> viewIds, String output_name) throws IOException, JSchException, SftpException {
		
		System.out.println("inputPath: " + spimdata.getBasePath() + "\ninterval: " + interval.toString() + "\n spimdata: "
				+ spimdata.getBasePath().getName() + "\n downsampling: " + downsampling + "\n viewIds: "
				+ viewIds.toString() + "\n output_name: " + output_name);
		
		File xml = XMLFile.fromStitchFolder(spimdata.getBasePath().getAbsolutePath());
		String inputPath = xml.getAbsolutePath();

		System.out.println("file: " + inputPath);
		
		int down;
		if (Double.isNaN(downsampling))
			down = 1;
		else
			down = (int) downsampling;
//		new ImageJ();

		MyLogger.initLogger();
		new Job();
		List<File> relatedFiles = XMLFile.initRelatedFiles(new File(inputPath));
		BoundingBox bb = new BoundingBox(interval);
		XMLFile inputFile = new XMLFile(inputPath, bb, spimdata, down, viewIds, relatedFiles);

		Login.login();

		ClusterFile clusterFolderName = new ClusterFile(Login.getServer().getPath(), Job.getId());
		N5File outputFile = new N5File(Job.file(output_name).getAbsolutePath(), inputFile.getDims(),BlockConfig.BLOCK_UNIT);
		System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

		Map<Integer, BasicBlockInfo> blocksInfo = MetadataGenerator.generateBlocks(inputFile.bb(),
				outputFile.getBlocksize());
		// BlocksMetaData2 md = new BlocksMetaData2(blocksInfo,
		// Util.int2long(outputFile.getBlocksize()), inputFile,blocksInfo.size());
		BlocksMetaData md = new BlocksMetaData(viewIds, blocksInfo, Util.int2long(outputFile.getBlocksize()),BlockConfig.BLOCK_UNIT,
				bb.getDimensions(down), blocksInfo.size(),down);
		File metadataFile = Job.file("metadata.json");
		MyLogger.log.info(md.toString());
		Job.setTotalbBlocks(md.getTotal());
		// md.toJson(metadataFile);
		GsonIO.toJson(md, metadataFile);

		// Create project folder in the cluster
		// SCPManager.createClusterFolder(clusterFolderName);

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
		BatchScriptFile.generate(batchScriptFile, clusterFolderName.getPath(), md.getTotal()); // md.getTotal()

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

	public static void run(String input_path, String output_name)
			throws SpimDataException, IOException, JSchException, SftpException {}
}
