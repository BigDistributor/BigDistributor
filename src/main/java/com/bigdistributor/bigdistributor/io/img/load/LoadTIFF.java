package net.preibisch.bigdistributor.io.img.load;

import java.io.File;
import java.io.IOException;

import net.imglib2.img.ImagePlusAdapter;

import com.google.common.io.Files;

import ij.ImageJ;
import ij.ImagePlus;
import loci.formats.Memoizer;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.bigdistributor.io.ImpOpener;
import net.preibisch.legacy.io.IOFunctions;
import net.preibisch.mvrecon.fiji.spimdata.imgloaders.filemap2.VirtualRAIFactoryLOCI;

public class LoadTIFF
{
	
	public static RandomAccessibleInterval< FloatType > load( final File file ) throws IncompatibleTypeException
	{
		final File tempDir = Files.createTempDir();
		if ( !file.exists() )
			throw new RuntimeException( "File " + file + " does not exist.");
	
		Memoizer m = new Memoizer(0L, tempDir);
	   return  new VirtualRAIFactoryLOCI().createVirtualCached( m, file, 0, 0, 0 , new FloatType(), null);
	}
	
	public static RandomAccessibleInterval< FloatType > loadWithoutCache( final File file ) throws IOException
	{
		return ImagePlusAdapter.wrap(ImpOpener.openImp(file));
	}

	public static void main( String[] args ) throws IncompatibleTypeException
	{
		new ImageJ();
		IOFunctions.printIJLog = true;

		new LoadTIFF();
		// get a virtual, cached RandomAccessible Interval
		final RandomAccessibleInterval< FloatType > virtualCached = LoadTIFF.load(new File( "/Users/Marwan/Desktop/Task/example_dataset/affine.tif") );

		// display
		final ImagePlus imp = ImageJFunctions.show( virtualCached );
		imp.setDimensions( 1, (int)virtualCached.dimension( 2 ), 1 );
		imp.setSlice( (int)virtualCached.dimension( 2 )/2 );
		imp.setDisplayRange( 0, 255 );

	}
}
