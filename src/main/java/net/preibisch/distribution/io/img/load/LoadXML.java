package net.preibisch.distribution.io.img.load;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ij.ImageJ;
import ij.ImagePlus;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.Angle;
import mpicbg.spim.data.sequence.Channel;
import mpicbg.spim.data.sequence.Illumination;
import mpicbg.spim.data.sequence.ImgLoader;
import mpicbg.spim.data.sequence.Tile;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import mpicbg.spim.io.IOFunctions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class LoadXML
{
	final File file;
	final SpimData2 spimData;

	public LoadXML( final String xml )
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
		final RandomAccessibleInterval< FloatType > virtual = FusionTools.fuseVirtual( spimData, viewIds, interval, downsampling ).getA();

		return virtual;
	}

	public static void main( String[] args )
	{
		new ImageJ();
		IOFunctions.printIJLog = true;

		final LoadXML load = new LoadXML( "/home/mzouink/Desktop/example_dataset/dataset.xml" );

		// select all views to process
		final List< ViewId > viewIds = new ArrayList< ViewId >();
		viewIds.addAll( load.spimData.getSequenceDescription().getViewDescriptions().values() );

		// or some manually selected
		//viewIds.add( new ViewId( 0, 0 ) );
		//viewIds.add( new ViewId( 0, 1 ) );
		//viewIds.add( new ViewId( 0, 2 ) );

		for ( final ViewId viewId : viewIds )
		{
			final ViewDescription vd = load.spimData.getSequenceDescription().getViewDescription( viewId );
			
			final int tpId = vd.getTimePointId();
			final int setupId = vd.getViewSetupId();
			
			final Channel channel = vd.getViewSetup().getChannel();
			final Illumination illum = vd.getViewSetup().getIllumination();
			final Tile tile = vd.getViewSetup().getTile();
			final Angle angle = vd.getViewSetup().getAngle();

			ImgLoader loader = load.spimData.getSequenceDescription().getImgLoader();

			// this is a 3d image stack - for 1 specific illum, tile, angle & channel
			RandomAccessibleInterval img = loader.getSetupImgLoader( viewId.getViewSetupId() ).getImage( viewId.getTimePointId() );
			// img.numDimensions() = 3;
			// it can be 2d, but then it is a 3d image with img.dimension( 2 ) = 1 
		}

		// all defined a bounding boxes
		for ( final BoundingBox bb : load.spimData.getBoundingBoxes().getBoundingBoxes() )
			IOFunctions.println( bb );
		

		// now the bounding box, I put quite a lot of options here of how to get one, you can play around with it
		Interval bb;

		// select a bounding box
		//bb = load.spimData.getBoundingBoxes().getBoundingBoxes().get( 0 );

		// bounding box around everything
		//bb = new BoundingBoxMaximal( viewIds, load.spimData ).estimate( "Full Bounding Box" );

		// bounding box with BDV
		//bb = new BoundingBoxBigDataViewer( load.spimData, viewIds ).estimate( "BDV bounding box" );

		// or just define manually (261, 332, 5) >>> (1057, 773, 387)
		bb = new FinalInterval( new long[]{ 261, 332, 5 }, new long[]{ 1057, 773, 387 });

		// get and display
		final RandomAccessibleInterval< FloatType > fused = fuse(load.spimData, viewIds, bb );

		final ImagePlus imp = ImageJFunctions.show( fused );
		imp.setDimensions( 1, (int)fused.dimension( 2 ), 1 );
		imp.setSlice( (int)fused.dimension( 2 )/2 );
		imp.setDisplayRange( 0, 255 );
	}


}
