package main.java.net.preibisch.distribution.io.img.n5;

import java.io.File;
import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import main.java.net.preibisch.distribution.io.IOFunctions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class testSaveBlockN5 {
public static void main(String[] args) throws IOException {
	String p1 = "/home/mzouink/Desktop/test/block.tif";

	String out = "/home/mzouink/Desktop/test/example.n5";
	
	
	File blockf = file(p1);
	RandomAccessibleInterval<FloatType> block = IOFunctions.openAs32Bit(blockf);
	System.out.println(string(getDimension(block)));
	
	ImageJFunctions.show(block,"Blcok");

	ImageJFunctions.show(new LoadN5(out).fuse(),"out");
	long[] position = new long[] {0,0,0};
	save(out,block,position);
	
	ImageJFunctions.show(new LoadN5(out).fuse(),"out-after");
}



private static String string(long[] dimension) {
	String st = "";
	for (long d : dimension)st += d + " " ;
	return st;
}



private static long[] getDimension(RandomAccessibleInterval<FloatType> block) {
	long[] dim = new long[block.numDimensions()];
	for(int i = 0; i< block.numDimensions();i++) dim[i] = block.dimension(i);
	return dim;
}



private static void save(String out, RandomAccessibleInterval<FloatType> block, long[] position) throws IOException {
	String dataset = "/volumes/raw";
	N5Writer writer = new N5FSWriter(out);
	N5Utils.saveBlock(block, writer, dataset, position);	
}

private static File file(String path) {
	File file = new File(path);
	if (!file.exists()){
		System.out.println("file not exist ! ");
		return null;
	}
	return file;
	
}

}
