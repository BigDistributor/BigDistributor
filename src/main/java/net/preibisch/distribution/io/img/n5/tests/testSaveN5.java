package net.preibisch.distribution.io.img.n5.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.process.fusion.FusionTools;
import net.preibisch.simulation.imgloader.SimulatedBeadsImgLoader;

public class testSaveN5 {
	public static void main(String[] args) throws IOException {
//		load();
	save();
	}

	private static void load() {
		String in = "/home/mzouink/Desktop/test/in.n5";

		String out = "/home/mzouink/Desktop/test/out.n5";
		
		LoadN5 loader = new LoadN5(out);
		RandomAccessibleInterval<FloatType> virtual = loader.fuse();
		new ImageJ();
		ImageJFunctions.show(virtual);
	}

	private static void save() throws IOException {
		// generate 4 views with 1000 corresponding beads, single timepoint
		SpimData2 spimData = SpimData2.convert(SimulatedBeadsImgLoader.spimdataExample(new int[] { 0, 90, 135 }));

		// load drosophila
		// spimData = new XmlIoSpimData2( "" ).load(
		// "/Users/spreibi/Documents/Microscopy/SPIM/HisYFP-SPIM/dataset.xml" );

		// select views to process
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimData.getSequenceDescription().getViewDescriptions().values());

		// small part of the bounding box\
		Interval bb = new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 100, 250, 350 });

		// downsampling
		double downsampling = Double.NaN; // 2.0

		// perforn the fusion virtually
		RandomAccessibleInterval<FloatType> virtual = FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling).getA();

		// make a physical copy of the virtual randomaccessibleinterval
		// final RandomAccessibleInterval< FloatType > fusedImg = FusionTools.copyImg(
		// virtual, new ImagePlusImgFactory<>( new FloatType() ), new FloatType(), null,
		// true );

		// save as one of the blocks of the N5
		new ImageJ();
		ImageJFunctions.show(virtual);

		String basePath = "/home/mzouink/Desktop/test/example.n5";
		String dataset = "/volumes/raw";
		N5Writer writer = new N5FSWriter(basePath);

		N5Utils.save(virtual, writer, dataset, new int[] { 80, 80, 80 }, new GzipCompression());
	}
}
