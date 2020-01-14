package net.preibisch.distribution.io.img.load;

import java.io.File;

import com.google.common.io.Files;

import ij.ImageJ;
import ij.ImagePlus;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.imgloaders.filemap2.VirtualRAIFactoryLOCI;

public class LoadTIFF
{

	public static RandomAccessibleInterval< FloatType > load( final String fileName ) throws IncompatibleTypeException
	{
		final File tempDir = Files.createTempDir();
		final File file = new File( fileName );

		if ( !file.exists() )
			throw new RuntimeException( "File " + fileName + " does not exist.");
		
//		IFormatReader reader = new Memoizer(new ImageReader(),Memoizer.DEFAULT_MINIMUM_ELAPSED,tempDir);
//		IFormatReader reader = 

		// loads the first timepoint, first channel, first series of the image
		return new VirtualRAIFactoryLOCI().createVirtualCached(
//				reader,
				null,
				file,
				0,
				0,
				0,
				new FloatType(),
				null );
	}

	public static void main( String[] args ) throws IncompatibleTypeException
	{
		new ImageJ();
		IOFunctions.printIJLog = true;

		new LoadTIFF();
		// get a virtual, cached RandomAccessible Interval
		final RandomAccessibleInterval< FloatType > virtualCached = LoadTIFF.load( "/Users/spreibi/Documents/Microscopy/SPIM/SPIMA_gfp1.tif" );

		// display
		final ImagePlus imp = ImageJFunctions.show( virtualCached );
		imp.setDimensions( 1, (int)virtualCached.dimension( 2 ), 1 );
		imp.setSlice( (int)virtualCached.dimension( 2 )/2 );
		imp.setDisplayRange( 0, 255 );

	}
}
