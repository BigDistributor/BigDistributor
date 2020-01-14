package net.preibisch.distribution.headless;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.clustering.ClusterFile;
import net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import net.preibisch.distribution.algorithm.clustering.kafka.KafkaMessageManager;
import net.preibisch.distribution.algorithm.clustering.scripting.BatchScriptFile;
import net.preibisch.distribution.algorithm.clustering.scripting.ClusterScript;
import net.preibisch.distribution.algorithm.clustering.scripting.JobType;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.server.Login;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.algorithm.task.FusionParams;
import net.preibisch.distribution.gui.PreviewUI;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;

public class ClusterWorkflow {

	private static final String BATCH_NAME = "_submit.cmd";
	private static final String TASK_SHELL_NAME = "_task.sh";

	public static void run(FusionParams params, String xml, String taskPath) throws IOException, JSchException, SftpException, SpimDataException {
		MyLogger.initLogger();
		new Job();
		List<File> relatedFiles = XMLFile.initRelatedFiles(new File(xml));
System.out.println("xa:"+xml);
		XMLFile inputFile = new XMLFile(xml, params.getBb(),params.getDownsampling(),
				params.getViewIds(), relatedFiles);
		PreviewUI ui = new PreviewUI(inputFile, params.getViewIds().size());
		Login.login();
		ClusterFile clusterFolderName = new ClusterFile(Login.getServer().getPath(), Job.getId());
		// SCPManager.createClusterFolder(clusterFolderName);

		File taskFile = new File(taskPath);
		inputFile.getRelatedFiles().add(taskFile);
		File inputCluster = clusterFolderName.subfile(inputFile);
		SCPManager.sendInput(inputFile, clusterFolderName);

		for (int i = 0; i < params.getViewIds().size(); i++) {
			List<ViewId> group = params.getViewIds().get(i);
			IOFunctions.println("group " + group);

			String output_name = i + "_output.n5";
			MyLogger.log.info(params.toString());

			N5File outputFile = new N5File(Job.file(output_name).getAbsolutePath(), inputFile.getDims(),
					BlockConfig.BLOCK_UNIT);
			System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

			Map<Integer, BasicBlockInfo> blocksInfo = MetadataGenerator.generateBlocks(inputFile.bb(),
					outputFile.getBlocksize());
			BlocksMetaData md = new BlocksMetaData(Job.getId(),  blocksInfo,
					Util.int2long(outputFile.getBlocksize()), BlockConfig.BLOCK_UNIT, 
					blocksInfo.size());
			File metadataFile = Job.file(i + "_metadata.json");
			MyLogger.log.info(md.toString());
			Job.setTotalbBlocks(md.getTotal());
			// md.toJson(metadataFile);
			GsonIO.toJson(md, metadataFile);
			File paramFile = Job.file(i + "_param.json");
			params.toJson(paramFile.getAbsolutePath());

			// Generate script

			String taskScriptName = i + TASK_SHELL_NAME;
			File scriptFile = Job.file(taskScriptName);
			File metadataCluster = clusterFolderName.subfile(metadataFile);
			File paramCluster = clusterFolderName.subfile(paramFile);
			System.out.println("Param cluster: "+paramCluster.getPath());
			File clusterOutput = clusterFolderName.subfile(outputFile);

			ClusterScript.generateTaskScript(scriptFile, taskFile.getName(), metadataCluster.getPath(),
					inputCluster.getPath(), clusterOutput.getPath(),paramCluster.getPath(),i);

			// Task to prepare N5
			String prepareScriptName = i + JobType.file(JobType.PREPARE);
			File prepareShell = Job.file(prepareScriptName);
			ClusterScript.generateTaskScript(JobType.PREPARE, prepareShell, taskFile.getName(),
					metadataCluster.getPath(), inputCluster.getPath(), clusterOutput.getPath(), paramCluster.getPath(),i);
	
			// Generate batch

			String batchScriptName = i + BATCH_NAME;
			File batchScriptFile = Job.file(batchScriptName);
			BatchScriptFile.generate(batchScriptFile, clusterFolderName.getPath(), md.getTotal(), i, prepareScriptName,
					taskScriptName); // md.getTotal()

			// send all
			List<File> toSend = new ArrayList<>();
			toSend.add(metadataFile);
			toSend.add(batchScriptFile);
			toSend.add(paramFile);
			toSend.add(scriptFile);
			toSend.add(prepareShell);

			SCPManager.send(toSend, clusterFolderName);

			// Run
			SCPManager.startBatch(clusterFolderName.subfile(batchScriptFile));

		}
		new KafkaMessageManager(Job.getId(), params.getViewIds().size());

	}
	
//	public static void run(SpimData2 spimdata, Interval interval, double downsampling,
//	List<Group<ViewDescription>> groups, File taskFile) throws IOException, JSchException, SftpException {
//
//File xmlFile = XMLFile.fromStitchFolder(spimdata.getBasePath().getAbsolutePath());
//String inputPath = xmlFile.getAbsolutePath();
//
//System.out.println("file: " + inputPath);
//
//int down;
//if (Double.isNaN(downsampling))
//	down = 1;
//else
//	down = (int) downsampling;
//// new ImageJ();
//
//MyLogger.initLogger();
//new Job();
//List<File> relatedFiles = XMLFile.initRelatedFiles(new File(inputPath));
//BoundingBox bb = new BoundingBox(interval);
//List<ViewId> viewIds = new ArrayList<>(groups.get(0).getViews());
//XMLFile inputFile = new XMLFile(inputPath, bb, spimdata, down, viewIds, relatedFiles);
//
//PreviewUI ui = new PreviewUI(inputFile, groups.size());
//
//Login.login();
//
//ClusterFile clusterFolderName = new ClusterFile(Login.getServer().getPath(), Job.getId());
//
//// SCPManager.createClusterFolder(clusterFolderName);
//
//inputFile.getRelatedFiles().add(taskFile);
//File inputCluster = clusterFolderName.subfile(inputFile);
//SCPManager.sendInput(inputFile, clusterFolderName);
//
//for (Group<ViewDescription> group : groups) {
//	IOFunctions.println("group " + group);
//
//	String output_name = i + "_output.n5";
//	MyLogger.log.info("inputPath: " + spimdata.getBasePath() + "\ninterval: " + interval.toString()
//			+ "\n spimdata: " + spimdata.getBasePath().getName() + "\n downsampling: " + downsampling
//			+ "\n viewIds: " + viewIds.toString() + "\n output_name: " + output_name);
//
//	N5File outputFile = new N5File(Job.file(output_name).getAbsolutePath(), inputFile.getDims(),
//			BlockConfig.BLOCK_UNIT);
//	System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));
//
//	Map<Integer, BasicBlockInfo> blocksInfo = MetadataGenerator.generateBlocks(inputFile.bb(),
//			outputFile.getBlocksize());
//	BlocksMetaData md = new BlocksMetaData(Job.getId(), viewIds, blocksInfo,
//			Util.int2long(outputFile.getBlocksize()), BlockConfig.BLOCK_UNIT, bb.getDimensions(down),
//			blocksInfo.size(), down);
//	File metadataFile = Job.file(i + "_metadata.json");
//	MyLogger.log.info(md.toString());
//	Job.setTotalbBlocks(md.getTotal());
//	// md.toJson(metadataFile);
//	GsonIO.toJson(md, metadataFile);
//	File paramFile = Job.file(i + "_param.json");
//	params.toJson(paramFile.getAbsolutePath());
//
//	// Generate script
//
//	String taskScriptName = i + TASK_SHELL_NAME;
//	File scriptFile = Job.file(taskScriptName);
//	File metadataCluster = clusterFolderName.subfile(metadataFile);
//
//	File clusterOutput = clusterFolderName.subfile(outputFile);
//
//	ClusterScript.generateTaskScript(scriptFile, taskFile.getName(), metadataCluster.getPath(),
//			inputCluster.getPath(), clusterOutput.getPath());
//
//	// Task to prepare N5
//	String prepareScriptName = i + TaskType.file(TaskType.PREPARE);
//	File prepareShell = Job.file(prepareScriptName);
//	ClusterScript.generateTaskScript(TaskType.PREPARE, prepareShell, taskFile.getName(),
//			metadataCluster.getPath(), inputCluster.getPath(), clusterOutput.getPath(), "");
//
//	// Generate batch
//
//	String batchScriptName = i + BATCH_NAME;
//	File batchScriptFile = Job.file(batchScriptName);
//	BatchScriptFile.generate(batchScriptFile, clusterFolderName.getPath(), md.getTotal(), i, prepareScriptName,
//			taskScriptName); // md.getTotal()
//
//	// send all
//	List<File> toSend = new ArrayList<>();
//	toSend.add(metadataFile);
//	toSend.add(batchScriptFile);
//	toSend.add(paramFile);
//	toSend.add(scriptFile);
//	toSend.add(prepareShell);
//
//	SCPManager.send(toSend, clusterFolderName);
//
//	// Run
//	SCPManager.startBatch(clusterFolderName.subfile(batchScriptFile));
//
//}
//new KafkaMessageManager(Job.getId(), groups.size());
//
//}
}
