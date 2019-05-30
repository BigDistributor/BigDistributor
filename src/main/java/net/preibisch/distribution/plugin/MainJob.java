package main.java.net.preibisch.distribution.plugin;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.RawCompression;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import main.java.net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import main.java.net.preibisch.distribution.tools.Tools;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MainJob implements Callable<Void> {
	@Option(names = { "-t", "--task" }, required = true, description = "The path of the Data")
	private String task;

	@Option(names = { "-o", "--output" }, required = true, description = "The path of the Data")
	private String output;

	@Option(names = { "-i", "--input" }, required = true, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = true, description = "The path of the MetaData file")
	private String metadataPath;

	@Option(names = { "-id" }, required = true, description = "The id of block")
	private Integer id;

	public MainJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void call() throws Exception {
		if (task == null)
			task = "pre";
		TaskType type = TaskType.of(task);
		switch (type) {
		case PREPARE:
			generateN5(input, metadataPath, output, id);
			break;
		case PROCESS:
			blockTask(input, metadataPath, output, id);
			break;

		default:
			throw new Exception("Specify task!");
		}
//			MyLogger.log.info("Block " + id + " saved !");
		return null;
	}

	public static void blockTask(String inputPath, String metadataPath, String outputPath, int id) {
		try {
			XMLFile inputFile = new XMLFile(inputPath);
			BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
			BasicBlockInfo binfo = md.getBlocksInfo().get(id);
			BoundingBox bb = new BoundingBox(Util.long2int(binfo.getMin()), Util.long2int(binfo.getMax()));
			RandomAccessibleInterval<FloatType> block = inputFile.fuse(bb);
			N5File outputFile = N5File.open(outputPath);
			outputFile.saveBlock(block, binfo.getGridOffset());
		} catch (SpimDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateN5(String inputPath, String metadataPath, String outputPath, int id) {
		try {
			System.out.println("Start geberating output");
			XMLFile inputFile = new XMLFile(inputPath);
			RandomAccessibleInterval<FloatType> virtual = inputFile.fuse();
			String dataset = "/volumes/raw";
			N5Writer writer = new N5FSWriter(outputPath);
			int[] blocks = Tools.array(BlockConfig.BLOCK_UNIT, virtual.numDimensions());

			N5Utils.save(virtual, writer, dataset, blocks, new RawCompression());
			System.out.println("Ouptut generated");
		} catch (SpimDataException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		try {
//			BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
//			long[] dims = md.getDimensions();
//			N5File outputFile = new N5File(outputPath, dims);
//			outputFile.create();
//		} catch (JsonSyntaxException | JsonIOException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static void main(String[] args) {
		System.out.println(String.join(" ", args));
		CommandLine.call(new MainJob(), args);
	}
}
