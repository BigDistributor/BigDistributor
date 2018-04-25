package master;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import blockmanager.Block;
import blockmanager.BlockGenerator;
import blockmanager.BlockGeneratorFixedSizePrecise;
import gui.MainFrame_;
import ij.ImageJ;
import multithreading.Threads;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import tools.Config;
import tools.Helper;
import tools.IOFunctions;

public class BlockExample
{
	public static void main( String[] args ) 
	{
		
		new ImageJ();
		
		IOFunctions.cleanFolder("input");
		String string = "img/DrosophilaWing.tif";
	
		Img<FloatType> image = IOFunctions.openAs32Bit( new File( string) );
		ImageJFunctions.show(image);

		final Img<FloatType> resultImage = new CellImgFactory< FloatType >( 64 ).create(Helper.getDimensions(image), new FloatType());

		final ExecutorService service = Threads.createExService( 1 );
		final long blockSize = 50;
		final long[] blockSizeDim = Util.getArrayFromValue( blockSize, image.numDimensions() );
		
		final BlockGenerator<Block> generator = new BlockGeneratorFixedSizePrecise( service, blockSizeDim );

		final double[] sigmas = Util.getArrayFromValue( (double)Config.getSigma(), image.numDimensions() );
		final int[] halfKernelSizes = Gauss3.halfkernelsizes( sigmas );

		//System.out.println( Util.printCoordinates( halfKernelSizes ));
		//for ( final double d : Gauss3.halfkernel( 6, halfKernelSizes[ 0 ], false ) )
		//	System.out.println( d );

		final long[] kernelSize = new long[ image.numDimensions() ];
		final long[] imgSize = new long[ image.numDimensions() ];

		for ( int d = 0; d < image.numDimensions(); ++d )
		{
			kernelSize[ d ] = halfKernelSizes[ d ] * 2 - 1;
			imgSize[ d ] = image.dimension( d );
		}
		
		final List< Block > blocks = generator.divideIntoBlocks( imgSize, kernelSize );
		final Img< FloatType > tmp = ArrayImgs.floats( blockSizeDim );

		final RandomAccessible< FloatType > infiniteImg = Views.extendMirrorSingle( image );

		int i = 0;

		final HashMap< Integer, Block > blockMap = new HashMap<>();

		for ( final Block block : blocks )
		{
			++i;

			// copy block (multithreaded)
			block.copyBlock( infiniteImg, tmp );

			blockMap.put( i, block );
			//IOFunctions.saveTiffStack( IOFunctions.getImagePlusInstance( tmp ), "input/part_"+i+".tif" );


			//save blocks into inputFolder
//			IOFunctions.saveBlocks(blocks);
			
			// process
			//for ( final FloatType f : tmp )
			//	f.set( i );
			
//			try { Gauss3.gauss( sigmas, Views.extendZero( tmp ), tmp, service ); } catch (IncompatibleTypeException e) {}
			
			// copy (/fast/AG_Preibisch?)
			// https://stackoverflow.com/questions/14617/how-to-retrieve-a-file-from-a-server-via-sftp

			// call cluster
			// login into cluster with ssh in Java
			// qsub ...
			
			// check when jobs are ready ... Timer?

			// receive (/fast/AG_Preibisch?)
			// https://stackoverflow.com/questions/14617/how-to-retrieve-a-file-from-a-server-via-sftp
		}


		for ( final Integer key : blockMap.keySet() )
		{
			// copy back
			blockMap.get( key ).pasteBlock( resultImage, tmp );			
		}

		ImageJFunctions.show(resultImage);		

		//IOFunctions.saveTiffStack( IOFunctions.getImagePlusInstance( resultImage ), "img/DrosophilaWing_gauss.tif" );

		final Img<FloatType> resultImage2 = image.copy();
		try { Gauss3.gauss( Config.getSigma(), Views.extendMirrorSingle( resultImage2 ), resultImage2 ); } catch (IncompatibleTypeException e) {}
		ImageJFunctions.show(resultImage2);

		System.out.println( imageDifference( resultImage, resultImage2 ) );
	}

	public static double imageDifference( final RandomAccessibleInterval< FloatType> img1, final RandomAccessibleInterval< FloatType > img2 )
	{
		final Cursor<FloatType> c1 = Views.iterable( img1 ).localizingCursor();
		final RandomAccess<FloatType> r2 = img2.randomAccess();

		double sumChange = 0;

		while ( c1.hasNext() )
		{
			c1.fwd();
			r2.setPosition( c1 );

			sumChange += Math.abs( c1.get().get() - r2.get().get() );
		}

		return sumChange;
	}
}
