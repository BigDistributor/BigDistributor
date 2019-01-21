package main.java.net.preibisch.distribution.input.imageaccess;

import java.io.File;
import java.util.Date;
import java.util.List;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class LoadN5 {
	final File file;
	final SpimData2 spimData;

	public LoadN5( final String xml )
	{
		this.file = new File( xml );

		if ( !file.exists() )
			throw new RuntimeException( "File " + xml + " does not exist.");

		try
		{
			this.spimData = new XmlIoSpimData2( "" ).load( file.getAbsolutePath() );
		}
		catch ( SpimDataException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new RuntimeException( "Could not load xml: " + e );
		}
	}

	public static RandomAccessibleInterval< FloatType > fuse(final SpimData2 spimData, final List< ViewId > viewIds, final Interval interval )
	{
		// filter not present ViewIds
		final List< ViewId > removed = SpimData2.filterMissingViews( spimData, viewIds );
		IOFunctions.println( new Date( System.currentTimeMillis() ) + ": Removed " +  removed.size() + " views because they are not present." );

		// downsampling
		double downsampling = Double.NaN;

		// display virtually fused
		final RandomAccessibleInterval< FloatType > virtual = FusionTools.fuseVirtual( spimData, viewIds, interval, downsampling );

		return virtual;
	}

	public static void main( String[] args )
	{
		
	}


}
