package net.preibisch.distribution.testapp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import ij.ImageJ;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.blockmanager.block.BlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.io.img.ImgFile;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.distribution.tools.Threads;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class TestFusionSpimInBlocksN5 {
	public static void main(String[] args) throws IOException, SpimDataException {

		final String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
		final String output_path = "/home/mzouink/Desktop/testn5/back_ output45.n5";

		new ImageJ();
		MyLogger.initLogger();

		XMLFile inputFile = XMLFile.XMLFile(input_path);

		// perform the fusion virtually
		ImageJFunctions.show(inputFile.fuse(), "Input");

		MyLogger.log.info("BB: " + inputFile.bb().toString());
		MyLogger.log.info("Dims: " + Util.printCoordinates(inputFile.getDims()));

//		N5File outputFile = N5File.fromXML(inputFile, output_path);
		N5File outputFile = new N5File(output_path, inputFile.getDims());
		MyLogger.log.info("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

		Map<Integer, BasicBlockInfo> blocks = MetadataGenerator.generateBlocks(inputFile.bb(), outputFile.getBlocksize());
		BlocksMetaData md = new BlocksMetaData(blocks, Util.int2long(outputFile.getBlocksize()), inputFile.getDimensions(1),blocks.size());
		int total = md.getBlocksInfo().size();
		System.out.println(md.toString());

		outputFile.create();

		ImageJFunctions.show(outputFile.fuse(), "Black output");

		ExecutorService executor = Threads.createExService();

		for (int i = 0; i < total; i++) {
			executor.submit(new Task(i, inputFile, outputFile, md.getBlocksInfo().get(i)));
		}
	}
}

class Task implements Runnable {
	private int i;
	private XMLFile input;
	private N5File output;
	private BasicBlockInfo binfo;

	public Task(int i, ImgFile input, N5File output, BlockInfo binfo) {
		this.i = i;
		this.input = (XMLFile) input;
		this.output = output;
		this.binfo = (BasicBlockInfo) binfo;
	}

	@Override
	public void run() {
		try {
			MyLogger.log.info("Started " + i);
			BoundingBox bb = new BoundingBox(Util.long2int(binfo.getMin()), Util.long2int(binfo.getMax()));
			RandomAccessibleInterval<FloatType> block = input.fuse(bb,0);
			output.saveBlock(block, binfo.getGridOffset());
			MyLogger.log.info("Block " + i + " saved !");
		} catch (IOException e) {
			MyLogger.log.error("ERROR: Block " + i);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
