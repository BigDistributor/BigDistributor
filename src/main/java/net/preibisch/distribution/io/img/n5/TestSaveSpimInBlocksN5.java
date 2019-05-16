package main.java.net.preibisch.distribution.io.img.n5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.janelia.saalfeldlab.n5.AbstractDataBlock;
import org.janelia.saalfeldlab.n5.BlockReader;
import org.janelia.saalfeldlab.n5.DataBlock;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.headless.MainJob;
import main.java.net.preibisch.distribution.tools.Tools;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.process.fusion.FusionTools;
import net.preibisch.simulation.imgloader.SimulatedBeadsImgLoader;

public class TestSaveSpimInBlocksN5 {
	public static void main(String[] args) throws InterruptedException, IOException, SpimDataException {

		new ImageJ();
		MyLogger.initLogger();
		final String tmpDir = "/home/mzouink/Desktop/testsave/";
		final String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
		final String output_path = "/home/mzouink/Desktop/testn5/output3.n5";

		Tools.cleanFolder(tmpDir);

		SpimData2 spimData = SpimData2.convert(SimulatedBeadsImgLoader.spimdataExample(new int[] { 0, 0, 0 }));
//		SpimData2 spimData = new XmlIoSpimData2( "" ).load(input_path);
		
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimData.getSequenceDescription().getViewDescriptions().values());

		// small part of the bounding box\
		Interval bb = new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 200, 300, 200 });

		// downsampling
		double downsampling = Double.NaN; // 2.0
//		double downsampling = 2; // 2.0

		// perforn the fusion virtually
		RandomAccessibleInterval<FloatType> source = FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling);

		ImageJFunctions.show(source, "Input");
		
		long[] dims = Tools.dimensions(source);
		System.out.println("Dims: " + Util.printCoordinates(dims));

		int[] blockSize  = new int[dims.length];
		Arrays.fill(blockSize , 80);
		System.out.println("Blocks: " + Util.printCoordinates(blockSize));
		BlocksMetaData md = MetaDataGenerator.genarateMetaData(dims, Util.int2long(blockSize), 0, new Callback());

		int total = md.getBlocksInfo().size();
		System.out.println(md.toString());
		/*
		N5Writer n5 = new N5FSWriter(output_path);
		final DatasetAttributes attributes = new DatasetAttributes(
				dims,
				blockSize,
				N5Utils.dataType(Util.getTypeFromInterval(source)),
				new GzipCompression());

		String dataset = "/volumes/raw" ;
		n5.createDataset(dataset , attributes);
		System.out.println("dataset created : "+ output_path);
		
		
		
		RandomAccessibleInterval<FloatType> virtual = N5Utils.open(new N5FSReader(output_path), dataset);
		ImageJFunctions.show(virtual,"Black output");

		saveBlock(0,source,n5,dataset,output_path,new long[] {1,1,1},new long[] {80,80,80},new long[] {0,0,0});

		saveBlock(2,source,n5,dataset,output_path,new long[] {1,81,1},new long[] {80,160,80},new long[] {0,1,0});


		saveBlock(3,source,n5,dataset,output_path,new long[] {81,81,1},new long[] {160,160,80},new long[] {1,1,0});

		/*
		long[] dims = Tools.dimensions(source);
		MyLogger.log.info("Dims: " + Util.printCoordinates(dims));

		final long[] blocksSizes = Tools.fill(150, dims.length);

		BlocksMetaData md = MetaDataGenerator.genarateMetaData(dims, blocksSizes, 0, new Callback());

		int total = md.getBlocksInfo().size();
//		System.out.println(md.toString());

//		String metadataPath = MetaDataGenerator.createJSon(md, tmpDir, new Callback());

		N5IO.createBackResult2(output, dims, Util.long2int(blocksSizes),
				N5Utils.dataType(Util.getTypeFromInterval(source)));

		N5IO.show(output, "Black output");

//		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i =0; i< total; i++) {
					
			BlockInfos binfo = md.getBlocksInfo().get(i);
			bb = new FinalInterval(binfo.getOffset(), Tools.sum(binfo.getOffset(),binfo.getEffectiveSize()));
			MyLogger.log.info(bb.toString());
			// perforn the fusion virtually
			final RandomAccessibleInterval< FloatType > blockimg = FusionTools.fuseVirtual( spimData, viewIds, bb, downsampling );
			ImageJFunctions.show( blockimg,i+"- block Input" );
			
			MyLogger.log.info("Process: "+i+"-save block size = "+Util.printCoordinates(Tools.dimensions(blockimg))+"position: "+Util.printCoordinates(binfo.getGridOffset()));
//			
			N5IO.saveBlock(output, blockimg, binfo.getGridOffset());
			MyLogger.log.info("Process: "+i+ "- Finish saving!");
			N5IO.show(output,i+"- output");
	
		}
*/
	}
	
	private static void saveBlock(int i, RandomAccessibleInterval<FloatType> source, N5Writer n5, String dataset,
			String output_path, long[] ls, long[] ls2, long[] grid) throws IOException {
		RandomAccessibleInterval< FloatType > block0 = Views.interval( source,ls,ls2);
		ImageJFunctions.show(block0,"block "+i);

		N5Utils.saveBlock(block0, n5, dataset,  grid);
		
		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
		
		ImageJFunctions.show(virtual2,"After block "+i+" output");
	}
}
