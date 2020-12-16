package net.preibisch.bigdistributor.io.img.n5.tests;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.bigdistributor.algorithm.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import net.preibisch.bigdistributor.io.img.xml.XMLFile;
import net.preibisch.bigdistributor.tools.helpers.ArrayHelpers;
import org.janelia.saalfeldlab.n5.*;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import java.io.IOException;

public class saveN5 {

	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";

	private final static String output_path = "/home/mzouink/Desktop/testn5/testN5.n5";
public static void main(String[] args) throws IOException, SpimDataException {
	System.out.println("Start generating output");
	XMLFile inputFile = XMLFile.create(input_path,Double.NaN);
	RandomAccessibleInterval<FloatType> virtual = inputFile.getImg();
	ImageJFunctions.show(virtual,"input");
	String dataset = "/volumes/raw";
	N5Writer writer = new N5FSWriter(output_path);
	int[] blocks = ArrayHelpers.array((int) BasicBlockInfoGenerator.BLOCK_SIZE, virtual.numDimensions());
	System.out.println("start saving output");
	N5Utils.save(virtual, writer, dataset, blocks, new RawCompression());
	System.out.println("Ouptut generated");
	
	N5Reader reader = new N5FSReader(output_path);
	RandomAccessibleInterval<FloatType> n5Image = N5Utils.open(reader , dataset);
	ImageJFunctions.show(n5Image,"n5Image");
}
}
