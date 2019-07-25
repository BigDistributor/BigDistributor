package net.preibisch.distribution.algorithm.multithreading;

import java.util.Vector;

public class ImagePortion
{
	public ImagePortion( final long startPosition, long loopSize )
	{
		this.startPosition = startPosition;
		this.loopSize = loopSize;
	}
	
	public long getStartPosition() { return startPosition; }
	public long getLoopSize() { return loopSize; }
	
	protected long startPosition;
	protected long loopSize;
	
	@Override
	public String toString() { return "Portion [" + getStartPosition() + " ... " + ( getStartPosition() + getLoopSize() - 1 ) + " ]"; }

	public static final Vector<ImagePortion> divideIntoPortions( final long imageSize )
	{
		int numPortions;

		if ( imageSize <= Threads.numThreads() )
			numPortions = (int)imageSize;
		else
			numPortions = Math.max( Threads.numThreads(), (int)( imageSize / ( 64l*64l*64l ) ) );

		//System.out.println( "nPortions for copy:" + numPortions );

		final Vector<ImagePortion> portions = new Vector<ImagePortion>();

		if ( imageSize == 0 )
			return portions;

		long threadChunkSize = imageSize / numPortions;

		while ( threadChunkSize == 0 )
		{
			--numPortions;
			threadChunkSize = imageSize / numPortions;
		}

		long threadChunkMod = imageSize % numPortions;

		for ( int portionID = 0; portionID < numPortions; ++portionID )
		{
			// move to the starting position of the current thread
			final long startPosition = portionID * threadChunkSize;

			// the last thread may has to run longer if the number of pixels cannot be divided by the number of threads
			final long loopSize;
			if ( portionID == numPortions - 1 )
				loopSize = threadChunkSize + threadChunkMod;
			else
				loopSize = threadChunkSize;
			
			portions.add( new ImagePortion( startPosition, loopSize ) );
		}
		
		return portions;
	}

}
