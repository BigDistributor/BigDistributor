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
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfo;
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import net.preibisch.distribution.algorithm.clustering.kafka.KafkaMessageManager;
import net.preibisch.distribution.algorithm.clustering.scripting.JobType;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.clustering.scripting.cluster.SGE.SGEClusterScript;
import net.preibisch.distribution.algorithm.clustering.scripting.cluster.SGE.SGESubmitFile;
import net.preibisch.distribution.algorithm.clustering.server.Connection;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.Metadata;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.task.params.ParamsJsonSerialzer;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.distribution.io.img.xml.XMLFile;
import net.preibisch.distribution.tools.helpers.ArrayHelpers;

public class ClusterWorkflow {

	private static final String BATCH_NAME = "_submit.cmd";
	private static final String TASK_SHELL_NAME = "_task.sh";

	public static void run(List<ParamsJsonSerialzer<?>> params, String input, TaskType type) throws IOException, JSchException, SftpException, SpimDataException {
		Job.create();
//		List<File> relatedFiles = XMLFile.initRelatedFiles(new File(xml));
		XMLFile inputFile = XMLFile.XMLFile(input);
		long[] bsizes = ArrayHelpers.fill(BasicBlockInfoGenerator.BLOCK_SIZE, inputFile.getDimensions().length);
//		PreviewUI ui = new PreviewUI(inputFile, params.getViewIds().size());
//		Connection.login();
		// SCPManager.createClusterFolder(clusterFolderName);
		
		File taskFile = new File(TaskType.getTaskFile(type));
		inputFile.getRelatedFiles().add(taskFile);
		String inputCluster = Job.get().subfile(inputFile.getName());
		SCPManager.sendInput(inputFile, Job.get().getCluster());

		for (int i = 0; i < params.size(); i++) {

			String output_name = i + "_output.n5";
			MyLogger.log().info(params.toString());

//			N5File outputFile = new N5File(Job.get().file(output_name).getAbsolutePath(), inputFile.getDims(),
//					(int)BasicBlockInfoGenerator.BLOCK_SIZE);
//			System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));
			String outptutCluster = Job.get().subfile(output_name);
			Map<Integer, BasicBlockInfo> blocksInfo = BasicBlockInfoGenerator.divideIntoBlockInfo(inputFile.bb());
			Metadata md = new Metadata(Job.get().getId(),inputCluster,outptutCluster, bsizes,blocksInfo);

			File metadataFile = Job.get().file(i + "_metadata.json");
			MyLogger.log().info(md.toString());
			Job.get().setTotalbBlocks(md.size());
			// md.toJson(metadataFile);
			GsonIO.toJson(md, metadataFile);
			File paramFile = Job.get().file(i + "_param.json");
			params.get(i).toJson(paramFile);

			// Generate script

			String taskScriptName = i + TASK_SHELL_NAME;
			File scriptFile = Job.get().file(taskScriptName);
			
			String metadataCluster =Job.get().subfile(metadataFile.getName());
			
			String paramCluster = Job.get().subfile(paramFile.getName());
			System.out.println("Param cluster: "+paramCluster);
			

			SGEClusterScript.generateTaskScript(scriptFile, taskFile.getName(), metadataCluster,
					inputCluster, outptutCluster,paramCluster,i);

			// Task to prepare N5
			String prepareScriptName = i + JobType.file(JobType.PREPARE);
			File prepareShell = Job.get().file(prepareScriptName);
			SGEClusterScript.generateTaskScript(JobType.PREPARE, prepareShell, taskFile.getName(),
					metadataCluster, inputCluster, outptutCluster, paramCluster,i);
	
			// Generate batch

			String batchScriptName = i + BATCH_NAME;
			File batchScriptFile = Job.get().file(batchScriptName);
			SGESubmitFile.generate(batchScriptFile, Job.get().getCluster().getAbsolutePath(), md.size(), i, prepareScriptName,
					taskScriptName); // md.getTotal()

			// send all
			List<File> toSend = new ArrayList<>();
			toSend.add(metadataFile);
			toSend.add(batchScriptFile);
			toSend.add(paramFile);
			toSend.add(scriptFile);
			toSend.add(prepareShell);

			SCPManager.send(toSend, Job.get().getCluster());

			// Run
			
			SCPManager.startBatch(new File(Job.get().getCluster().getPath(),batchScriptFile.getName()));

		}
		new KafkaMessageManager(Job.get().getId(), params.size());

	}
}
