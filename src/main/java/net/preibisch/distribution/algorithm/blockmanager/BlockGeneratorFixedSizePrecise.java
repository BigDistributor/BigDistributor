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
package net.preibisch.distribution.algorithm.blockmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;

public class BlockGeneratorFixedSizePrecise implements BlockGenerator< Block >
{
	final long[] blockSize;
	final ExecutorService service;

	public BlockGeneratorFixedSizePrecise(
			final ExecutorService service,
			final long[] blockSize )
	{
		this.blockSize = blockSize;
		this.service = service;
	}

	/**
	 * Divides an image into blocks
	 * 
	 * @param imgSize - the size of the image
	 * @param kernelSize - the size of the kernel (has to be odd!)
	 * @return array of blocks
	 */
	@Override
	public ArrayList< Block > divideIntoBlocks( final long[] imgSize, final long[] kernelSize, AbstractCallBack callback )
	{
		final int numDimensions = imgSize.length;
		
		// compute the effective size & local offset of each block
		// this is the same for all blocks
		final long[] effectiveSizeGeneral = new long[ numDimensions ];
		final long[] effectiveLocalOffset = new long[ numDimensions ];
		
		for ( int d = 0; d < numDimensions; ++d )
		{
			effectiveSizeGeneral[ d ] = blockSize[ d ] - kernelSize[ d ] + 1;
			
			if ( effectiveSizeGeneral[ d ] <= 0 )
			{
				callback.log( "Blocksize in dimension " + d + " (" + blockSize[ d ] + ") is smaller than the kernel (" + kernelSize[ d ] + ") which results in an negative effective size: " + effectiveSizeGeneral[ d ] + ". Quitting." );
				return null;
			}
			
			effectiveLocalOffset[ d ] = kernelSize[ d ] / 2;
		}
		
		// compute the amount of blocks needed
		final long[] numBlocks = new long[ numDimensions ];

		for ( int d = 0; d < numDimensions; ++d )
		{
			numBlocks[ d ] = imgSize[ d ] / effectiveSizeGeneral[ d ];
			
			// if the modulo is not 0 we need one more that is only partially useful
			if ( imgSize[ d ] % effectiveSizeGeneral[ d ] != 0 )
				++numBlocks[ d ];
		}
		
		MyLogger.log.info( "imgSize " + Util.printCoordinates( imgSize ) );
		MyLogger.log.info( "kernelSize " + Util.printCoordinates( kernelSize ) );
		MyLogger.log.info( "blockSize " + Util.printCoordinates( blockSize ) );
		MyLogger.log.info( "numBlocks " + Util.printCoordinates( numBlocks ) );
		MyLogger.log.info( "effectiveSize of blocks" + Util.printCoordinates( effectiveSizeGeneral ) );
		MyLogger.log.info( "effectiveLocalOffset " + Util.printCoordinates( effectiveLocalOffset ) );
				
		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator( numBlocks );
		final ArrayList< Block > blockList = new ArrayList< Block >();

		final int[] currentBlock = new int[ numDimensions ];

		while ( cursor.hasNext() )
		{
			cursor.fwd();
			cursor.localize( currentBlock );

			// compute the current offset
			final long[] offset = new long[ numDimensions ];
			final long[] effectiveOffset = new long[ numDimensions ];
			final long[] effectiveSize = effectiveSizeGeneral.clone();

			for ( int d = 0; d < numDimensions; ++d )
			{
				effectiveOffset[ d ] = currentBlock[ d ] * effectiveSize[ d ];
				offset[ d ] = effectiveOffset[ d ] - kernelSize[ d ]/2;

				if ( effectiveOffset[ d ] + effectiveSize[ d ] > imgSize[ d ] )
					effectiveSize[ d ] = imgSize[ d ] - effectiveOffset[ d ];
			}

			blockList.add( new Block( service, blockSize, offset, effectiveSize, effectiveOffset, effectiveLocalOffset, true ) );
			//MyLogger.log.info( "block " + Util.printCoordinates( currentBlock ) + " effectiveOffset: " + Util.printCoordinates( effectiveOffset ) + " effectiveSize: " + Util.printCoordinates( effectiveSize )  + " offset: " + Util.printCoordinates( offset ) + " inside: " + inside );
		}

		return blockList;
	}
	
	@Override
	public HashMap< Integer, Block > divideIntoHashMapBlocks( final long[] imgSize, final long[] kernelSize )
	{
		final int numDimensions = imgSize.length;
		
		// compute the effective size & local offset of each block
		// this is the same for all blocks
		final long[] effectiveSizeGeneral = new long[ numDimensions ];
		final long[] effectiveLocalOffset = new long[ numDimensions ];
		
		for ( int d = 0; d < numDimensions; ++d )
		{
			effectiveSizeGeneral[ d ] = blockSize[ d ] - kernelSize[ d ] + 1;
			
			if ( effectiveSizeGeneral[ d ] <= 0 )
			{
				MyLogger.log.info( "Blocksize in dimension " + d + " (" + blockSize[ d ] + ") is smaller than the kernel (" + kernelSize[ d ] + ") which results in an negative effective size: " + effectiveSizeGeneral[ d ] + ". Quitting." );
				return null;
			}
			
			effectiveLocalOffset[ d ] = kernelSize[ d ] / 2;
		}
		
		// compute the amount of blocks needed
		final long[] numBlocks = new long[ numDimensions ];

		for ( int d = 0; d < numDimensions; ++d )
		{
			numBlocks[ d ] = imgSize[ d ] / effectiveSizeGeneral[ d ];
			
			// if the modulo is not 0 we need one more that is only partially useful
			if ( imgSize[ d ] % effectiveSizeGeneral[ d ] != 0 )
				++numBlocks[ d ];
		}
		
		MyLogger.log.info( "imgSize " + Util.printCoordinates( imgSize ) );
		MyLogger.log.info( "kernelSize " + Util.printCoordinates( kernelSize ) );
		MyLogger.log.info( "blockSize " + Util.printCoordinates( blockSize ) );
		MyLogger.log.info( "numBlocks " + Util.printCoordinates( numBlocks ) );
		MyLogger.log.info( "effectiveSize of blocks" + Util.printCoordinates( effectiveSizeGeneral ) );
		MyLogger.log.info( "effectiveLocalOffset " + Util.printCoordinates( effectiveLocalOffset ) );
				
		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator( numBlocks );
//		final ArrayList< Block > blockList = new ArrayList< Block >();

		HashMap< Integer, Block > hashMapList = new HashMap<>();
		
		final int[] currentBlock = new int[ numDimensions ];
		int i = 0;

		while ( cursor.hasNext() )
		{
			
			cursor.fwd();
			cursor.localize( currentBlock );

			// compute the current offset
			final long[] offset = new long[ numDimensions ];
			final long[] effectiveOffset = new long[ numDimensions ];
			final long[] effectiveSize = effectiveSizeGeneral.clone();

			for ( int d = 0; d < numDimensions; ++d )
			{
				effectiveOffset[ d ] = currentBlock[ d ] * effectiveSize[ d ];
				offset[ d ] = effectiveOffset[ d ] - kernelSize[ d ]/2;

				if ( effectiveOffset[ d ] + effectiveSize[ d ] > imgSize[ d ] )
					effectiveSize[ d ] = imgSize[ d ] - effectiveOffset[ d ];
			}

			hashMapList.put(i, new Block( service, blockSize, offset, effectiveSize, effectiveOffset, effectiveLocalOffset, true ) );
			i++;
			//MyLogger.log.info( "block " + Util.printCoordinates( currentBlock ) + " effectiveOffset: " + Util.printCoordinates( effectiveOffset ) + " effectiveSize: " + Util.printCoordinates( effectiveSize )  + " offset: " + Util.printCoordinates( offset ) + " inside: " + inside );
		}

		return hashMapList;
	}

}
