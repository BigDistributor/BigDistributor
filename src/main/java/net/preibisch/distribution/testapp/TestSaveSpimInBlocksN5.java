package main.java.net.preibisch.distribution.testapp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BlockInfo;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.DataExtension;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import main.java.net.preibisch.distribution.io.img.ImgFile;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.io.img.n5.N5File;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class TestSaveSpimInBlocksN5 {
	public static void main(String[] args) throws IOException, SpimDataException {

		final String tmpDir = "/home/mzouink/Desktop/testsave/";
		final String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
		final String output_path = "/home/mzouink/Desktop/testn5/output45.n5";

		new ImageJ();
		MyLogger.initLogger();

		XMLFile InputFile = null;
		if (DataExtension.fromURI(input_path) == DataExtension.XML) {
			InputFile = new XMLFile(input_path, false);
		}

		// perform the fusion virtually
		ImageJFunctions.show(InputFile.fuse(), "Input");

		System.out.println("BB: " + InputFile.bb().toString());
		System.out.println("Dims: " + Util.printCoordinates(InputFile.getDims()));

		N5File outputFile = N5File.fromXML(InputFile, output_path);
		System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

		BlocksMetaData md = MetadataGenerator.genarateMetaData(InputFile.bb(), outputFile.getBlocksize());
		int total = md.getBlocksInfo().size();
		System.out.println(md.toString());

		outputFile.create();

		ImageJFunctions.show(outputFile.fuse(), "Black output");

		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < total; i++) {
			executor.submit(new Task(i, InputFile, outputFile, md.getBlocksInfo().get(i)));
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
			BoundingBox bb = new BoundingBox(Util.long2int(binfo.getMin()), Util.long2int(binfo.getMax()));
			RandomAccessibleInterval<FloatType> block = input.fuse(bb);
			output.saveBlock(block, binfo.getGridOffset());
			MyLogger.log.info("Block " + i + " saved !");
		} catch (IOException e) {
			MyLogger.log.info("ERROR: Block " + i);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
