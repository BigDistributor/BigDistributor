/*-
 * #%L
 * Software for the reconstruction of multi-view microscopic acquisitions
 * like Selective Plane Illumination Microscopy (SPIM) Data.
 * %%
 * Copyright (C) 2012 - 2017 Multiview Reconstruction developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */
package net.preibisch.bigdistributor.gui.bdv;

import bdv.AbstractSpimSource;
import bdv.tools.brightness.MinMaxGroup;
import bdv.tools.brightness.SetupAssignments;
import bdv.viewer.Source;
import bdv.viewer.ViewerPanel;
import bdv.viewer.state.ViewerState;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.DiscreteFrequencyDistribution;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.histogram.Real1dBinMapper;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.LinAlgHelpers;
import net.imglib2.view.Views;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BDVPopup
{
	
	/**
	 * set BDV brightness by sampling the mid z plane (and 1/4 and 3/4 if z is large enough )
	 * of the currently selected source (typically the first source) and getting quantiles from intensity histogram
	 * (slightly modified version of InitializeViewerState.initBrightness)
	 *
	 * @param cumulativeMinCutoff - quantile of min 
	 * @param cumulativeMaxCutoff - quantile of max
	 * @param state - Bdv's ViewerSate
	 * @param setupAssignments - Bdv's View assignments
	 * @param <T> - type extending RealType
	 */
	public static <T extends RealType<T>> void initBrightness( final double cumulativeMinCutoff, final double cumulativeMaxCutoff, final ViewerState state, final SetupAssignments setupAssignments )
	{
		final Source< ? > source = state.getSources().get( state.getCurrentSource() ).getSpimSource();
		final int timepoint = state.getCurrentTimepoint();
		if ( !source.isPresent( timepoint ) )
			return;
		if ( !RealType.class.isInstance( source.getType() ) )
			return;
		@SuppressWarnings( "unchecked" )
		final RandomAccessibleInterval< T > img = ( RandomAccessibleInterval< T > ) source.getSource( timepoint, source.getNumMipmapLevels() - 1 );
		final long z = ( img.min( 2 ) + img.max( 2 ) + 1 ) / 2;

		final int numBins = 6535;
		final Histogram1d< T > histogram = new Histogram1d< T >( Views.iterable( Views.hyperSlice( img, 2, z ) ), new Real1dBinMapper< T >( 0, 65535, numBins, false ) );

		// sample some more planes if we have enough
		if ( (img.max( 2 ) + 1 -  img.min( 2 ) ) > 4 )
		{
			final long z14 = ( img.min( 2 ) + img.max( 2 ) + 1 ) / 4;
			final long z34 = ( img.min( 2 ) + img.max( 2 ) + 1 ) / 4 * 3;
			histogram.addData(  Views.iterable( Views.hyperSlice( img, 2, z14 ) ) );
			histogram.addData(  Views.iterable( Views.hyperSlice( img, 2, z34 ) ) );
		}

		final DiscreteFrequencyDistribution dfd = histogram.dfd();
		final long[] bin = new long[] { 0 };
		double cumulative = 0;
		int i = 0;
		for ( ; i < numBins && cumulative < cumulativeMinCutoff; ++i )
		{
			bin[ 0 ] = i;
			cumulative += dfd.relativeFrequency( bin );
		}
		final int min = i * 65535 / numBins;
		for ( ; i < numBins && cumulative < cumulativeMaxCutoff; ++i )
		{
			bin[ 0 ] = i;
			cumulative += dfd.relativeFrequency( bin );
		}
		final int max = i * 65535 / numBins;
		final MinMaxGroup minmax = setupAssignments.getMinMaxGroups().get( 0 );
		minmax.getMinBoundedValue().setCurrentValue( min );
		minmax.getMaxBoundedValue().setCurrentValue( max );
	}

	public static void initTransform( final ViewerPanel viewer )
	{
		final Dimension dim = viewer.getDisplay().getSize();
		final ViewerState state = viewer.getState();
		final AffineTransform3D viewerTransform = initTransform( dim.width, dim.height, false, state );
		viewer.setCurrentViewerTransform( viewerTransform );
	}

	public static AffineTransform3D initTransform( final int viewerWidth, final int viewerHeight, final boolean zoomedIn, final ViewerState state )
	{
		final int cX = viewerWidth / 2;
		final int cY = viewerHeight / 2;

		final Source< ? > source = state.getSources().get( state.getCurrentSource() ).getSpimSource();
		final int timepoint = state.getCurrentTimepoint();
		if ( !source.isPresent( timepoint ) )
			return new AffineTransform3D();

		final AffineTransform3D sourceTransform = new AffineTransform3D();
		source.getSourceTransform( timepoint, 0, sourceTransform );

		final Interval sourceInterval = source.getSource( timepoint, 0 );
		final double sX0 = sourceInterval.min( 0 );
		final double sX1 = sourceInterval.max( 0 );
		final double sY0 = sourceInterval.min( 1 );
		final double sY1 = sourceInterval.max( 1 );
		final double sZ0 = sourceInterval.min( 2 );
		final double sZ1 = sourceInterval.max( 2 );
		final double sX = ( sX0 + sX1 + 1 ) / 2;
		final double sY = ( sY0 + sY1 + 1 ) / 2;
		final double sZ = ( sZ0 != 0 || sZ1 != 0 ) ? ( sZ0 + sZ1 + 1 ) / 2 : 0;

		final double[][] m = new double[ 3 ][ 4 ];

		// NO rotation
		final double[] qViewer = new double[]{ 1, 0, 0, 0 };
		LinAlgHelpers.quaternionToR( qViewer, m );

		// translation
		final double[] centerSource = new double[] { sX, sY, sZ };
		final double[] centerGlobal = new double[ 3 ];
		final double[] translation = new double[ 3 ];
		sourceTransform.apply( centerSource, centerGlobal );
		LinAlgHelpers.quaternionApply( qViewer, centerGlobal, translation );
		LinAlgHelpers.scale( translation, -1, translation );
		LinAlgHelpers.setCol( 3, translation, m );

		final AffineTransform3D viewerTransform = new AffineTransform3D();
		viewerTransform.set( m );

		// scale
		final double[] pSource = new double[] { sX1 + 0.5, sY1 + 0.5, sZ };
		final double[] pGlobal = new double[ 3 ];
		final double[] pScreen = new double[ 3 ];
		sourceTransform.apply( pSource, pGlobal );
		viewerTransform.apply( pGlobal, pScreen );
		final double scaleX = cX / pScreen[ 0 ];
		final double scaleY = cY / pScreen[ 1 ];
		final double scale;
		if ( zoomedIn )
			scale = Math.max( scaleX, scaleY );
		else
			scale = Math.min( scaleX, scaleY );
		viewerTransform.scale( scale );

		// window center offset
		viewerTransform.set( viewerTransform.get( 0, 3 ) + cX, 0, 3 );
		viewerTransform.set( viewerTransform.get( 1, 3 ) + cY, 1, 3 );
		return viewerTransform;
	}

	private static final void callLoadTimePoint( final AbstractSpimSource< ? > s, final int timePointIndex )
	{
		try
		{
			Class< ? > clazz = null;
			boolean found = false;
	
			do
			{
				if ( clazz == null )
					clazz = s.getClass();
				else
					clazz = clazz.getSuperclass();
	
				if ( clazz != null )
					for ( final Method method : clazz.getDeclaredMethods() )
						if ( method.getName().equals( "loadTimepoint" ) )
							found = true;
			}
			while ( !found && clazz != null );
	
			if ( !found )
			{
				System.out.println( "Failed to find SpimSource.loadTimepoint method. Quiting." );
				return;
			}
	
			final Method loadTimepoint = clazz.getDeclaredMethod( "loadTimepoint", Integer.TYPE );
			loadTimepoint.setAccessible( true );
			loadTimepoint.invoke( s, timePointIndex );
		}
		catch ( Exception e ) { e.printStackTrace(); }
	}

	private static final int getCurrentTimePointIndex( final AbstractSpimSource< ? > s )
	{
		try
		{
			Class< ? > clazz = null;
			Field currentTimePointIndex = null;

			do
			{
				if ( clazz == null )
					clazz = s.getClass();
				else
					clazz = clazz.getSuperclass();

				if ( clazz != null )
					for ( final Field field : clazz.getDeclaredFields() )
						if ( field.getName().equals( "currentTimePointIndex" ) )
							currentTimePointIndex = field;
			}
			while ( currentTimePointIndex == null && clazz != null );

			if ( currentTimePointIndex == null )
			{
				System.out.println( "Failed to find AbstractSpimSource.currentTimePointIndex. Quiting." );
				return -1;
			}

			currentTimePointIndex.setAccessible( true );

			return currentTimePointIndex.getInt( s );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return -1;
		}
	}
}
