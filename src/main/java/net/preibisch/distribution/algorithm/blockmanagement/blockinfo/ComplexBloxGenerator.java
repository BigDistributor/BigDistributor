package net.preibisch.distribution.algorithm.blockmanagement.blockinfo;

import java.util.HashMap;
import java.util.Map;

import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.tools.helpers.ArrayHelpers;

public class ComplexBloxGenerator  {
	
	final static long BLOCK_SIZE = 32;
	
	public Map<Integer, ComplexBlockInfo> generateBlocks(long[] dims, long overlap) {
		final double[] sigmas = Util.getArrayFromValue((double) overlap, dims.length);
		final int[] halfKernelSizes = Gauss3.halfkernelsizes(sigmas);
		final long[] kernelSize = new long[dims.length];
		final long[] imgSize = new long[dims.length];
		for (int d = 0; d < dims.length; ++d) {
			kernelSize[d] = (overlap == 0) ? 0 : (halfKernelSizes[d] * 2 - 1);
			imgSize[d] = dims[d];
		}
	

		final Map<Integer, ComplexBlockInfo> blocks = divideIntoBlocks( imgSize, kernelSize);
		return blocks;
	}
	private static Map<Integer, ComplexBlockInfo> divideIntoBlocks(final long[] imgSize,
			final long[] kernelSize) {
		
		long[] blockSize =  ArrayHelpers.fill(BLOCK_SIZE, imgSize.length);
	
		final int numDimensions = imgSize.length;

		// compute the effective size & local offset of each block
		// this is the same for all blocks
		final long[] effectiveSizeGeneral = new long[numDimensions];
		final long[] effectiveLocalOffset = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
//			if (kernelSize[d] > 0) {
				effectiveSizeGeneral[d] = blockSize[d] - kernelSize[d] ;
//			} else {
//				effectiveSizeGeneral[d] = blockSize[d];
//			}

			if (effectiveSizeGeneral[d] <= 0) {
				MyLogger.log().info("Blocksize in dimension " + d + " (" + blockSize[d] + ") is smaller than the kernel ("
						+ kernelSize[d] + ") which results in an negative effective size: " + effectiveSizeGeneral[d]
						+ ". Quitting.");
				return null;
			}

			effectiveLocalOffset[d] = kernelSize[d] / 2;
		}

		// compute the amount of blocks needed
		final long[] numBlocks = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			numBlocks[d] = imgSize[d] / effectiveSizeGeneral[d];

			// if the modulo is not 0 we need one more that is only partially useful
			if (imgSize[d] % effectiveSizeGeneral[d] != 0)
				++numBlocks[d];
		}

		MyLogger.log().info("imgSize " + Util.printCoordinates(imgSize));
		MyLogger.log().info("kernelSize " + Util.printCoordinates(kernelSize));
		MyLogger.log().info("blockSize " + Util.printCoordinates(blockSize));
		MyLogger.log().info("numBlocks " + Util.printCoordinates(numBlocks));
		MyLogger.log().info("effectiveSize of blocks" + Util.printCoordinates(effectiveSizeGeneral));
		MyLogger.log().info("effectiveLocalOffset " + Util.printCoordinates(effectiveLocalOffset));

		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator(numBlocks);
		final Map<Integer, ComplexBlockInfo> blockinfosList = new HashMap<>();

		final int[] currentBlock = new int[numDimensions];
		int i = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(currentBlock);
			final long[] gridOffset = Util.int2long(currentBlock);
			
			// compute the current offset
			final long[] offset = new long[numDimensions];
			final long[] x1 = new long[numDimensions];
			final long[] x2 = new long[numDimensions];
			final long[] effectiveSize = effectiveSizeGeneral.clone();

			for (int d = 0; d < numDimensions; d++) {
				x1[d] = currentBlock[d] * effectiveSize[d] + 1;
				offset[d] = x1[d] - kernelSize[d] / 2;

				if (x1[d] + effectiveSize[d] > imgSize[d])
					effectiveSize[d] = imgSize[d] - x1[d];
				x2[d] = x1[d]+effectiveSize[d]-1;
			}

			blockinfosList.put(i,
					new ComplexBlockInfo(gridOffset, blockSize, effectiveSize,x1, x2, offset, effectiveLocalOffset,effectiveLocalOffset, true));
			i++;
		}

		return blockinfosList;
	}
}
