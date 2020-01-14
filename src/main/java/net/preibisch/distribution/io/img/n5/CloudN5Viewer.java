package net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class CloudN5Viewer {
	public static void main(String[] args) throws IOException {
		new ImageJ();
		String path = "/Volumes/AG_Preibisch/Marwan/clustering/8f7746b435564f92bbb38ed6293f09fc/0_output.n5";
		String dataset = "/volumes/raw";
		N5Reader n5 = new N5FSReader(path);
		
		long time = System.currentTimeMillis();
		RandomAccessibleInterval<FloatType> img = N5Utils.open(n5, dataset);
		img = FusionTools.copyImg(img, new CellImgFactory(), new FloatType(), null, true );
		System.out.println("loaded in " + ( System.currentTimeMillis() - time ) + " msec.");
		ImageJFunctions.show(img);
		//RandomAccessibleInterval<FloatType> img = N5Utils.openWithBoundedSoftRefCache(n5, dataset, 100000000);
		//Bdv bdv = BdvFunctions.show(img, "img");
	}
}
