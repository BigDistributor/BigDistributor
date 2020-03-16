package net.preibisch.distribution.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.Interval;
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
import net.preibisch.distribution.algorithm.task.params.SerializableParams;
import net.preibisch.distribution.io.DistributedFile;
import net.preibisch.distribution.io.FileStatus;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.distribution.io.TaskFile;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.distribution.io.img.xml.XMLFile;
import net.preibisch.distribution.tools.helpers.ArrayHelpers;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class ClusterWorkflow {

	private static final String BATCH_NAME = "_submit.cmd";
	private static final String TASK_SHELL_NAME = "_task.sh";

	private static final String TASK_PATH = "/fast/AG_Preibisch/Marwan/clustering/jars/FusionV2.jar";
	private static final String INPUT_PATH = "/fast/AG_Preibisch/Marwan/clustering/20200312153816/dataset.xml";

	public static void run(List<SerializableParams> params, String input, TaskType type, Interval interval) throws SpimDataException, IOException, JSchException, SftpException {
		Job.create();
//		XMLFile inputFile = XMLFile.create(INPUT_PATH, 0, FileStatus.IN_CLUSTER);
		DistributedFile inputFile = new DistributedFile(INPUT_PATH, FileStatus.IN_CLUSTER);
		BoundingBox bb = params.get(0).getBoundingBox();
		long[] dims = bb.getDimensions(1);
		long[] bsizes = ArrayHelpers.fill(BasicBlockInfoGenerator.BLOCK_SIZE, dims.length);
//		File taskFile = new File(TaskType.getTaskFile(type));
		TaskFile task = new TaskFile(TASK_PATH,type,FileStatus.IN_CLUSTER); 
		task.prepare();
//		inputFile.getRelatedFiles().add(taskFile);
//		String inputCluster = Job.get().subfile(inputFile.getName());
//		SCPManager.sendInput(inputFile, Job.get().getCluster());

		for (int i = 0; i < params.size(); i++) {

			String output_name = i + "_output.n5";
			MyLogger.log().info(params.toString());

			String outptutCluster = Job.get().subfile(output_name);
			Map<Integer, BasicBlockInfo> blocksInfo = BasicBlockInfoGenerator.divideIntoBlockInfo(bb);
			Metadata md = new Metadata(Job.get().getId(),inputFile.getClusterPath(),outptutCluster,new BoundingBox(interval), bsizes,blocksInfo);

			File metadataFile = Job.get().file(i + "_metadata.json");
			MyLogger.log().info(md.toString());
			Job.get().setTotalbBlocks(md.size());
			GsonIO.toJson(md, metadataFile);
			File paramFile = Job.get().file(i + "_param.json");
			params.get(i).toJson(paramFile);

			// Generate script

			String taskScriptName = i + TASK_SHELL_NAME;
			File scriptFile = Job.get().file(taskScriptName);
			
			String metadataCluster =Job.get().subfile(metadataFile.getName());
			
			String paramCluster = Job.get().subfile(paramFile.getName());
			System.out.println("Param cluster: "+paramCluster);
			

			SGEClusterScript.generateTaskScript(JobType.PROCESS,scriptFile, task.getPath(), metadataCluster,
					inputFile.getClusterPath(), outptutCluster,paramCluster);

			// Task to prepare N5
			String prepareScriptName = i + JobType.file(JobType.PREPARE);
			File prepareShell = Job.get().file(prepareScriptName);
			SGEClusterScript.generateTaskScript(JobType.PREPARE, prepareShell, task.getPath(),
					metadataCluster, inputFile.getClusterPath(), outptutCluster, paramCluster);
	
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
//		new KafkaMessageManager(Job.get().getId(), params.size());

	}
}
