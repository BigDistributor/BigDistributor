package net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class CloudN5Viewer {
	public static void main(String[] args) throws IOException {
		String path = "/Volumes/AG_Preibisch/Marwan/clustering/a9777e56e7b64bf7a033958d9dc6cf97/0_output.n5";
		String dataset = "/volumes/raw";
		N5Reader n5 = new N5FSReader(path);
		RandomAccessibleInterval<FloatType> img = N5Utils.openWithBoundedSoftRefCache(n5, dataset, 100000000);
		ImageJFunctions.show(img);
	}
}
