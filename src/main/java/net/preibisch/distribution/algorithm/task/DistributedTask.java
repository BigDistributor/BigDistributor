package net.preibisch.distribution.algorithm.task;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.preibisch.distribution.algorithm.clustering.kafka.KafkaManager;
import net.preibisch.distribution.algorithm.clustering.scripting.JobType;
import net.preibisch.distribution.algorithm.controllers.items.Metadata;
import net.preibisch.distribution.io.img.n5.N5File;
import picocli.CommandLine.Option;

public class DistributedTask implements Callable<Void> {
	@Option(names = { "-t", "--task" }, required = true, description = "The path of the Data")
	private String task;

	@Option(names = { "-o", "--output" }, required = true, description = "The path of the Data")
	private String output;

	@Option(names = { "-i", "--input" }, required = true, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = true, description = "The path of the MetaData file")
	private String metadataPath;
	
	@Option(names = { "-p", "--param" }, required = true, description = "The path of the MetaData file")
	private String paramPath;
	
	@Option(names = { "-id" }, required = false, description = "The id of block")
	private Integer id;

	private BlockTask mainTask;

	public DistributedTask(BlockTask task) {
		this.mainTask = task;
	}
	@Override
	public Void call() throws Exception {
		
		try {
			System.out.println("id: "+id);
			id = id - 1;
		} catch (Exception e) {
			KafkaManager.error(-1, e.toString());
			System.out.println("Error id");
			throw new Exception("Specify id!");
		}
//		try {
			System.out.println("task: "+task);
			JobType type = JobType.of(task);
			
			switch (type) {
			case PREPARE:
				System.out.println(type.toString());
				generateN5(input, metadataPath, output);
				return null;
			case PROCESS:
				System.out.println(type.toString());
				mainTask.blockTask(input, metadataPath, paramPath, output, id);
				return null;
			}
		return null;
	}

	private void generateN5(String inputPath, String metadataPath, String outputPath) {
		try {
			
			Metadata md = Metadata.fromJson(metadataPath);
			String jobId = md.getJobID();
			KafkaManager.jobID = jobId;
			KafkaManager.log(-1, "Start generate n5");
			long[] dims = md.getBb().getDimensions(1);
			N5File outputFile = new N5File(outputPath, dims);
			outputFile.create();
			KafkaManager.log(-1, "N5 Generated");
			KafkaManager.done(-1, "N5 Generated");
		} catch (JsonSyntaxException | JsonIOException | IOException e) {

			KafkaManager.error(-1, e.toString());
			e.printStackTrace();
		}
	}

}
