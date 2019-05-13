package main.java.net.preibisch.distribution.io.img.n5;

import java.io.IOException;
import java.util.Arrays;

import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import main.java.net.preibisch.distribution.tools.Tools;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

public class N5IO {
	private final static String DATASET = "/volumes/raw";

	public static void saveBlock(String out, RandomAccessibleInterval<FloatType> block, long[] position)
			throws IOException {
		N5Writer writer = new N5FSWriter(out);
		N5Utils.saveBlock(block, writer, DATASET, position);
	}

	
	public static void createBackResult(String path, long[] dimensions, int[] blocks) throws IOException {
		// make a physical copy of the virtual randomaccessibleinterval
		ImgFactory<FloatType> imgFactory = new CellImgFactory<>(new FloatType(), 5);
		final RandomAccessibleInterval<FloatType> img = imgFactory.create(dimensions);
		N5Writer writer = new N5FSWriter(path);
		N5Utils.save(img, writer, DATASET, blocks, new GzipCompression());
	}

	
	public static RandomAccessibleInterval<FloatType> read(String path) throws IOException {
		N5Reader reader = new N5FSReader(path);
		RandomAccessibleInterval<FloatType> virtual = N5Utils.open(reader, DATASET);
		return virtual;
	}
	

	public static void save(String path, RandomAccessibleInterval<FloatType> img, int[] blocks) throws IOException {
		N5Writer writer = new N5FSWriter(path);
		N5Utils.save(img, writer, DATASET, blocks, new GzipCompression());
	}
	
	public static void show(String path) throws IOException {
		RandomAccessibleInterval<FloatType> virtual = read(path);
		ImageJFunctions.show(virtual);
	}
	
	public static void main(String[] args) throws IOException {
		new ImageJ();
		show("/home/mzouink/Desktop/test2/out.n5");
	}
}
