package main.java.net.preibisch.distribution.io.img.n5;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.io.IOFunctions;
import main.java.net.preibisch.distribution.tools.Tools;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class testSaveBlockN5 {
	public static void main(String[] args) throws IOException {

		new ImageJ();

		MyLogger.initLogger();
		final String input_path = "/home/mzouink/Desktop/testn5/inputx4.tif";
		final String output_path = "/home/mzouink/Desktop/testn5/output2.n5";

		RandomAccessibleInterval<FloatType> source = IOFunctions.openAs32Bit(new File(input_path ));
		ImageJFunctions.show(source, "Input");

		long[] dims = Tools.dimensions(source);
		System.out.println("Dims: " + Util.printCoordinates(dims));

		int[] blockSize  = new int[dims.length];
		Arrays.fill(blockSize , 80);
		System.out.println("Blocks: " + Util.printCoordinates(blockSize));
		
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
		
//		RandomAccessibleInterval< FloatType > block0 = Views.interval( source, new long[] {0,0,0},new long[] {80,80,80});
//		ImageJFunctions.show(block0,"block 0");
//
//		N5Utils.saveBlock(block0, n5, dataset,  new long[] {0,0,0});
//		
//		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
//		
//		ImageJFunctions.show(virtual2,"After block0 output");
		BlocksMetaData md = MetaDataGenerator.genarateMetaData(dims, Util.int2long(blockSize), 0, new Callback());
		
		int total = md.getBlocksInfo().size();
		
		
//		saveBlock(0,source,n5,dataset,output_path,new long[] {1,1,1},new long[] {80,80,80},new long[] {0,0,0});
//		saveBlock(2,source,n5,dataset,output_path,new long[] {1,81,1},new long[] {80,160,80},new long[] {0,1,0});
//		saveBlock(3,source,n5,dataset,output_path,new long[] {81,81,1},new long[] {160,160,80},new long[] {1,1,0});
//	}

		for(int i =0; i< total; i++) {
			
			saveBlock(i,source,n5,dataset,output_path,md.getBlocksInfo().get(i));	
		}
		

	}
	
	private static void saveBlock(int i, RandomAccessibleInterval<FloatType> source, N5Writer n5, String dataset,
			String output_path,BlockInfos binfo) throws IOException {
		RandomAccessibleInterval< FloatType > block = Views.interval( source,binfo.getX1(),binfo.getX2());
		ImageJFunctions.show(block,"block "+i);

		N5Utils.saveBlock(block, n5, dataset,  binfo.getGridOffset());
		
		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
		
		ImageJFunctions.show(virtual2,"After block "+i+" output");
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
