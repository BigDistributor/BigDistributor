package net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.DataType;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class N5IO {
	private final static String DATASET = "/volumes/raw";

	public static void saveBlock(String out, RandomAccessibleInterval<FloatType> block, long[] gridOffset)
			throws IOException {
		N5Writer writer = new N5FSWriter(out);
		N5Utils.saveBlock(block, writer, DATASET, gridOffset);
	}

	
	public static void createBackResult(String path, long[] dimensions, int[] blockSize) throws IOException {
		// make a physical copy of the virtual randomaccessibleinterval
		ImgFactory<FloatType> imgFactory = new CellImgFactory<>(new FloatType(), 5);
		final RandomAccessibleInterval<FloatType> img = imgFactory.create(dimensions);
		N5Writer writer = new N5FSWriter(path);
		N5Utils.save(img, writer, DATASET, blockSize, new GzipCompression());
	}

	public static <T> void createBackResult2(String path, long[] dimensions, int[] blockSize,DataType datatype) throws IOException {

		N5Writer n5 = new N5FSWriter(path);
		long[] dims = dimensions.clone();
//		for (int i =0; i< dims.length; i++) dims[i]+=150;
		
		final DatasetAttributes attributes = new DatasetAttributes(
				dims,
				blockSize,
				datatype,
				new GzipCompression());

		n5.createDataset(DATASET, attributes);
		
	}
	
	public static RandomAccessibleInterval<FloatType> read(String path) throws IOException {
		N5Reader reader = new N5FSReader(path);
		RandomAccessibleInterval<FloatType> virtual = N5Utils.open(reader, DATASET);
		return virtual;
	}
	
	public static RandomAccessibleInterval<FloatType> readBlock(String path, long[] gridPosition) throws IOException {
		N5Reader reader = new N5FSReader(path);
		RandomAccessibleInterval<FloatType> virtual = N5Utils.open(reader, DATASET);
		return virtual;
	}
	

	public static void save(String path, RandomAccessibleInterval<FloatType> img, int[] blocks) throws IOException {
		N5Writer writer = new N5FSWriter(path);
		N5Utils.save(img, writer, DATASET, blocks, new GzipCompression());
	}
	
	public static void show(String path,String title) throws IOException {
		RandomAccessibleInterval<FloatType> virtual = read(path);
		ImageJFunctions.show(virtual,title);
//		MyLogger.log.info(title+"- "+Util.printCoordinates(Tools.dimensions(virtual)));
	}
	
	public static void main(String[] args) throws IOException {
		new ImageJ();
//		show("/home/mzouink/Desktop/testn5/output2.n5","test");

		show("/home/mzouink/Desktop/testn5/output45.n5","test");
	}
}
