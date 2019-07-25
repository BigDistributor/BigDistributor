package net.preibisch.distribution.io.img.n5.tests;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.RawCompression;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.tools.Tools;

public class saveN5 {

	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";

	private final static String output_path = "/home/mzouink/Desktop/testn5/testN5.n5";
public static void main(String[] args) throws SpimDataException, IOException {
	System.out.println("Start generating output");
	XMLFile inputFile = XMLFile.XMLFile(input_path);
	RandomAccessibleInterval<FloatType> virtual = inputFile.fuse();
	ImageJFunctions.show(virtual,"input");
	String dataset = "/volumes/raw";
	N5Writer writer = new N5FSWriter(output_path);
	int[] blocks = Tools.array(BlockConfig.BLOCK_UNIT, virtual.numDimensions());
	System.out.println("start saving output");
	N5Utils.save(virtual, writer, dataset, blocks, new RawCompression());
	System.out.println("Ouptut generated");
	
	N5Reader reader = new N5FSReader(output_path);
	RandomAccessibleInterval<FloatType> n5Image = N5Utils.open(reader , dataset);
	ImageJFunctions.show(n5Image,"n5Image");
}
}
