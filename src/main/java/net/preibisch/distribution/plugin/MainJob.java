package main.java.net.preibisch.distribution.plugin;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import main.java.net.preibisch.distribution.algorithm.AbstractTask2;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MainJob implements Callable<Void> {

	@Option(names = { "-o", "--output" }, required = true, description = "The path of the Data")
	private String output;

	@Option(names = { "-i", "--input" }, required = true, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = true, description = "The path of the MetaData file")
	private String metadataPath;

	@Option(names = { "-id" }, required = true, description = "The id of block")
	private Integer id;

	private AbstractTask2<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<FloatType>, Object> task;

	public MainJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void call() {

		try {
			blockTask(input, metadataPath, output, id);
			MyLogger.log.info("Block " + id + " saved !");
		} catch (JsonSyntaxException | JsonIOException | SpimDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void blockTask(String inputPath, String metadataPath, String outputPath, int id)
			throws SpimDataException, JsonSyntaxException, JsonIOException, IOException {
		XMLFile inputFile = new XMLFile(inputPath);
		BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
		BasicBlockInfo binfo = md.getBlocksInfo().get(id);
		BoundingBox bb = new BoundingBox(Util.long2int(binfo.getMin()), Util.long2int(binfo.getMax()));
		RandomAccessibleInterval<FloatType> block = inputFile.fuse(bb);
		N5File outputFile = N5File.open(outputPath);
		outputFile.saveBlock(block, binfo.getGridOffset());
	}
	
	public static void main(String[] args) {
		System.out.println(String.join(" ", args));
		CommandLine.call(new MainJob(), args);
	}
}
