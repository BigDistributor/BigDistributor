package net.preibisch.distribution.headless;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.blockmanagement.block.Block;
import net.preibisch.distribution.algorithm.blockmanagement.block.BlockGeneratorFixedSizePrecise;
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import net.preibisch.distribution.algorithm.blockmanagement.io.BlockFileManager;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.multithreading.Threads;
import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.img.load.LoadTIFF;
import net.preibisch.distribution.tools.helpers.ArrayHelpers;
import net.preibisch.legacy.io.IOFunctions;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "SplitTiff", description = "Generate tiff blocks out of one big TiffImage "
			, version = "SplitTiff 0.1")

public class SplitTiffImageIntoBlocksImages implements Callable<Boolean> {
	@Option(names = { "-i", "--input" }, required = true, description = "The path of the input Image")
	private String input;

	@Option(names = { "-out", "--output" }, required = true, description = "The path of the output folder")
	private String output;

	@Option(names = { "-blockSize" }, required = false, description = "The block size")
	private Integer blockSize;

	public static void main(String[] args) {
		CommandLine.call(new SplitTiffImageIntoBlocksImages(), args);
		System.exit(0);
	}

	@Override
	public Boolean call() throws Exception {
		long size = blockSize != null ? blockSize : BasicBlockInfoGenerator.BLOCK_SIZE;
		
		File file = new File(input);
		if (!file.isFile())
			throw new Exception("Invalid Input !");
		
		File outFolder = new File(output);
		if (!outFolder.isDirectory())
			throw new Exception("Invalid Output !");
		RandomAccessibleInterval<FloatType> image = LoadTIFF.load(file.getAbsolutePath());
//		RandomAccessibleInterval<FloatType> image = IOFunctions.openAs32Bit(file);
		ExecutorService service = Threads.createExService(5);
		
		long[] blockSizes = ArrayHelpers.fill(size, image.numDimensions());
		MyLogger.log().info("Start process.. ");
		BlockGeneratorFixedSizePrecise generator = new BlockGeneratorFixedSizePrecise(service ,blockSizes );
		List<Block> blocks = generator.divideIntoBlocks(ArrayHelpers.dims(image), blockSizes);
		MyLogger.log().info("Block Generated.. ");
		BlockFileManager.saveAllBlocks(image, blocks , outFolder, DataExtension.TIF);
		MyLogger.log().info("Done !");
		return true;
	}
}
