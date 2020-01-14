package net.preibisch.distribution.algorithm.blockmanager.block;

import java.util.HashMap;
import java.util.Map;

import net.imglib2.Interval;
import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class BasicBlockGenerator {
	public static Map<Integer, BasicBlockInfo> divideIntoBlocks(final long[] blockSize, final long[] imgSize, AbstractCallBack callback) {
		
		final int numDimensions = imgSize.length;

		// compute the amount of blocks needed
		final long[] numBlocks = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			numBlocks[d] = imgSize[d] / blockSize[d];

			// if the modulo is not 0 we need one more that is only partially useful
			if (imgSize[d] % blockSize[d] != 0)
				++numBlocks[d];
		}

		MyLogger.log.info("imgSize " + Util.printCoordinates(imgSize));
		MyLogger.log.info("blockSize " + Util.printCoordinates(blockSize));
		MyLogger.log.info("numBlocks " + Util.printCoordinates(numBlocks));

		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator(numBlocks);
		final Map<Integer, BasicBlockInfo> blockinfosList = new HashMap<Integer, BasicBlockInfo>();

		final int[] currentBlock = new int[numDimensions];
		int i = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(currentBlock);
			final long[] gridOffset = Util.int2long(currentBlock);
			
			// compute the current offset
			//final long[] offset = new long[numDimensions];
			final long[] min = new long[numDimensions];
			final long[] max = new long[numDimensions];
			final long[] effectiveBlockSize = blockSize.clone();

			for (int d = 0; d < numDimensions; d++) {
				min[d] = currentBlock[d] * blockSize[d];

				if (min[d] + blockSize[d] > imgSize[d])
					effectiveBlockSize[d] = imgSize[d] - min[d];

				max[d] = min[d]+effectiveBlockSize[d]-1;
			}

			blockinfosList.put(i,
					new BasicBlockInfo(gridOffset,blockSize, effectiveBlockSize, min,max));
			i++;
//			if (i % 10 == 0) {
				MyLogger.log.info(
						i+"- block " + Util.printCoordinates(gridOffset) + "size " + Util.printCoordinates(blockSize)
								+ "x1: " + Util.printCoordinates(min) + " x2: " + Util.printCoordinates(max) + " effectiveSize: "
								+ Util.printCoordinates(effectiveBlockSize) + " offset: " + Util.printCoordinates(min));
//			}
		}

		return blockinfosList;
	}

	public static Map<Integer, BasicBlockInfo> divideIntoBlocks(Interval bb, long[] blockSize) {

		long[] imgSize = new BoundingBox(bb).getDimensions(1);
		
		final int numDimensions = blockSize.length;

		// compute the amount of blocks needed
		final long[] numBlocks = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			numBlocks[d] = imgSize[d] / blockSize[d];

			// if the modulo is not 0 we need one more that is only partially useful
			if (imgSize[d] % blockSize[d] != 0)
				++numBlocks[d];
		}

		MyLogger.log.info("imgSize " + Util.printCoordinates(imgSize));
		MyLogger.log.info("blockSize " + Util.printCoordinates(blockSize));
		MyLogger.log.info("numBlocks " + Util.printCoordinates(numBlocks));

		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator(numBlocks);
		final Map<Integer, BasicBlockInfo> blockinfosList = new HashMap<Integer, BasicBlockInfo>();

		final int[] currentBlock = new int[numDimensions];
		int i = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(currentBlock);
			final long[] gridOffset = Util.int2long(currentBlock);
			
			// compute the current offset
			//final long[] offset = new long[numDimensions];
			final long[] min = new long[numDimensions];
			final long[] max = new long[numDimensions];
			final long[] effectiveBlockSize = blockSize.clone();

			for (int d = 0; d < numDimensions; d++) {
//				System.out.println(d+"-Min bb: "+bb.min(d));
				min[d] = currentBlock[d] * blockSize[d]+bb.min(d);

				if (min[d] + blockSize[d] > imgSize[d]+bb.min(d))
					effectiveBlockSize[d] = imgSize[d] - min[d] + bb.min(d);

				max[d] = min[d]+effectiveBlockSize[d]-1;
			}

			blockinfosList.put(i,
					new BasicBlockInfo(gridOffset,blockSize, effectiveBlockSize, min,max));
			i++;
//			if (i % 10 == 0) {
				MyLogger.log.info(
						i+"- block " + Util.printCoordinates(gridOffset) + "size " + Util.printCoordinates(blockSize)
								+ "x1: " + Util.printCoordinates(min) + " x2: " + Util.printCoordinates(max) + " effectiveSize: "
								+ Util.printCoordinates(effectiveBlockSize) + " offset: " + Util.printCoordinates(min));
//			}
		}

		return blockinfosList;
	
	}
}
