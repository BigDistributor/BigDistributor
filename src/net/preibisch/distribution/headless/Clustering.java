package net.preibisch.distribution.headless;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.Interval;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.clustering.ClusterFile;
import net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import net.preibisch.distribution.algorithm.clustering.scripting.BatchScriptFile;
import net.preibisch.distribution.algorithm.clustering.scripting.ClusterScript;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.server.Login;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.mvrecon.fiji.plugin.fusion.FusionGUI;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.interestpointregistration.pairwise.constellation.grouping.Group;

public class Clustering {
	private static final String BATCH_NAME = "submit.cmd";
	private static final String TASK_SHELL_NAME = "task.sh";

	private final static String task_path = "/Users/Marwan/Desktop/Task/Fusion.jar";

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

	public static void run(FusionGUI fusion) {
		IOFunctions.println( "CLUSTER!!!");
		
		fusion.getBoundingBox();
		

		final List< Group< ViewDescription > > groups = fusion.getFusionGroups();
		
		for ( Group<ViewDescription> group : groups )
		{
			IOFunctions.println( "group " + group );
			
			List<ViewId> viewIds = new ArrayList<>(group.getViews());
			
			try {
				String output_name = groups.indexOf(group)+ "_output.n5";
				Clustering.run(fusion.getBoundingBox(), fusion.getSpimData(), fusion.getDownsampling(), viewIds, output_name);
			} catch (IOException | JSchException | SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
