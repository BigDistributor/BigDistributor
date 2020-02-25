package net.preibisch.distribution.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanagement.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfo;
import net.preibisch.distribution.algorithm.clustering.ClusterFile;
import net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import net.preibisch.distribution.algorithm.clustering.kafka.KafkaMessageManager;
import net.preibisch.distribution.algorithm.clustering.scripting.BatchScriptFile;
import net.preibisch.distribution.algorithm.clustering.scripting.ClusterScript;
import net.preibisch.distribution.algorithm.clustering.scripting.JobType;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.clustering.server.Connection;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.task.params.ParamsJsonSerialzer;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;

public class ClusterWorkflow {

	private static final String BATCH_NAME = "_submit.cmd";
	private static final String TASK_SHELL_NAME = "_task.sh";

	public static void run(List<ParamsJsonSerialzer<?>> params, String xml, TaskType type) throws IOException, JSchException, SftpException, SpimDataException {
		Job.create();
//		List<File> relatedFiles = XMLFile.initRelatedFiles(new File(xml));
		XMLFile inputFile = XMLFile.XMLFile(xml);
//		PreviewUI ui = new PreviewUI(inputFile, params.getViewIds().size());
//		Connection.login();
		// SCPManager.createClusterFolder(clusterFolderName);
		ClusterFile clusterFolderName = new ClusterFile(Connection.getServer().getPath(), Job.get().getId());

		File taskFile = new File(TaskType.getTaskFile(type));
		inputFile.getRelatedFiles().add(taskFile);
		File inputCluster = clusterFolderName.subfile(inputFile);
		SCPManager.sendInput(inputFile, clusterFolderName);

		for (int i = 0; i < params.size(); i++) {

			String output_name = i + "_output.n5";
			MyLogger.log().info(params.toString());

			N5File outputFile = new N5File(Job.get().file(output_name).getAbsolutePath(), inputFile.getDims(),
					BlockConfig.BLOCK_UNIT);
			System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

			Map<Integer, BasicBlockInfo> blocksInfo = MetadataGenerator.generateBlocks(inputFile.bb(),
					outputFile.getBlocksize());
			BlocksMetaData md = new BlocksMetaData(Job.get().getId(),  blocksInfo,
					Util.int2long(outputFile.getBlocksize()), BlockConfig.BLOCK_UNIT, 
					blocksInfo.size());
			File metadataFile = Job.file(i + "_metadata.json");
			MyLogger.log().info(md.toString());
			Job.setTotalbBlocks(md.getTotal());
			// md.toJson(metadataFile);
			GsonIO.toJson(md, metadataFile);
			File paramFile = Job.file(i + "_param.json");
			params.get(i).toJson(paramFile);

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
		new KafkaMessageManager(Job.getId(), params.size());

	}
}
