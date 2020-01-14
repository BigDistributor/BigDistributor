package net.preibisch.distribution.io.img.n5.tests;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.RawCompression;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.IJ;
import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.io.IOTools;
import net.preibisch.distribution.tools.Tools;

public class testSaveBlockN5 {
	public static void main(String[] args) throws Exception {

		new ImageJ();

		MyLogger.initLogger();
		
		final String input_path = "/home/mzouink/Desktop/testn5/input.tif";
		final String output_path = "/home/mzouink/Desktop/testn5/output_big2.n5";

		final File out = new File(output_path);
		if ( out.exists() )
		{
			// delete
			Tools.deleteRecursively( out );
	
			if ( out.exists() )
				throw new RuntimeException("failed to delete: " + out.getAbsolutePath() );
			else
				System.out.println( "Deleted '"  + out.getAbsolutePath()  + "'" );
		}

		RandomAccessibleInterval<FloatType> source = IOTools.openAs32Bit(new File(input_path ));
		ImageJFunctions.show(source, "Input");

		long[] dims = Tools.dimensions(source);
		System.out.println("Dims: " + Util.printCoordinates(dims));
		//System.exit( 0 );

		int[] blockSize  = new int[dims.length];
		blockSize[ 0 ] = 977;
		blockSize[ 1 ] = 1000;
		blockSize[ 2 ] = 388;

		Arrays.fill(blockSize , 32);
		//blockSize[ 0 ] = 600;

		System.out.println("Blocks: " + Util.printCoordinates(blockSize));
		
		N5Writer n5 = new N5FSWriter(output_path);
		final DatasetAttributes attributes = new DatasetAttributes(
				dims,
				blockSize,
				N5Utils.dataType(Util.getTypeFromInterval(source)),
				new RawCompression() ); // new GzipCompression());

		String dataset = "/volumes/raw" ;
		n5.createDataset(dataset , attributes);
		
		System.out.println("dataset created : "+ output_path);
		
		//RandomAccessibleInterval<FloatType> virtual = N5Utils.open(new N5FSReader(output_path), dataset);
		//ImageJFunctions.show(virtual,"Black output");
		
//		RandomAccessibleInterval< FloatType > block0 = Views.interval( source, new long[] {0,0,0},new long[] {80,80,80});
//		ImageJFunctions.show(block0,"block 0");
//
//		N5Utils.saveBlock(block0, n5, dataset,  new long[] {0,0,0});
//		
//		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
//		
//		ImageJFunctions.show(virtual2,"After block0 output");
		BlocksMetaData md = MetadataGenerator.genarateMetaData(dims, Util.int2long(blockSize), 0, new Callback());
		
		int total = md.getBlocksInfo().size();
		
		
//		saveBlock(0,source,n5,dataset,output_path,new long[] {1,1,1},new long[] {80,80,80},new long[] {0,0,0});
//		saveBlock(2,source,n5,dataset,output_path,new long[] {1,81,1},new long[] {80,160,80},new long[] {0,1,0});
//		saveBlock(3,source,n5,dataset,output_path,new long[] {81,81,1},new long[] {160,160,80},new long[] {1,1,0});
//	}

		for(int i =0; i< total; i++) {
			
			saveBlock(i,source,n5,dataset,output_path,(BasicBlockInfo) md.getBlocksInfo().get(i),blockSize);	
			
			//SimpleMultiThreading.threadHaltUnClean();
			IJ.showProgress( i, total );
		}
		
		System.out.println( "Written ... " );

		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
		ImageJFunctions.show(virtual2,"After");
	}
	
	private static void saveBlock(int i, RandomAccessibleInterval<FloatType> source, N5Writer n5, String dataset,
			String output_path,BasicBlockInfo binfo, final int[] blockSize ) throws IOException {

		/*
		boolean fits = true;

		for ( int d = 0; d < source.numDimensions(); ++d )
			if ( binfo.getX2()[ d ] - binfo.getX1()[ d ] + 1 != blockSize[ d ] )
				fits = false;
		*/
		final RandomAccessibleInterval< FloatType > block;

		/*
		// blocksize of the image doesnt match the N5 blocksize (this is at the end of the image, last block in each dim)
		if ( fits == false )
		{
			RandomAccessible< FloatType> infinite = Views.extendZero(source);
			
			long[] x2 = new long[ source.numDimensions() ];

			for ( int d = 0; d < source.numDimensions(); ++d )
				x2[ d ] = binfo.getX1()[ d ] + blockSize[ d ] - 1;

			block = Views.interval( infinite, binfo.getX1(), x2 );	
		}
		else*/
		{
			block = Views.interval( source,binfo.getMin(),binfo.getMax());			
		}

		//ImageJFunctions.show(block,"block "+i);

		N5Utils.saveBlock(block, n5, dataset,  binfo.getGridOffset());
		
		//RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
		//ImageJFunctions.show(virtual2,"After block "+i+" output");
	}
	
	private static void saveBlock(int i, RandomAccessibleInterval<FloatType> source, N5Writer n5, String dataset,
			String output_path, long[] ls, long[] ls2, long[] grid) throws IOException {
		RandomAccessibleInterval< FloatType > block0 = Views.interval( source,ls,ls2);
		
		if ( i == 0 )
			ImageJFunctions.show(block0,"block "+i);

		N5Utils.saveBlock(block0, n5, dataset,  grid);
		
		/*
		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
		
		ImageJFunctions.show(virtual2,"After block "+i+" output");*/
	}
	


}
