package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BlockInfo;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.EffectiveBlockInfo;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.tools.config.Config;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.util.Util;

public class MetaDataGenerator implements AbstractTask {
	private final static String METADATA_FILE_NAME = "METADATA.json";

	@Override
	public void start(int pos, AbstractCallBack callback) {
		callback.log("Creating metadata..");
		final DataPreview data = Config.getDataPreview();
		BlocksMetaData md = genarateMetaData(data, callback);
		Config.getJob().setMetaDataPath(createJSon(md, Config.getJob().getTmpDir(), callback));
		callback.onSuccess(pos);
	}

	public static String genarateMetaDataFile(DataPreview data, String folder, AbstractCallBack callback) {

		callback.log("Creating metadata..");
		BlocksMetaData md = genarateMetaData(data, callback);
		return createJSon(md, folder, callback);
	}

	public static String createJSon(BlocksMetaData md, String folder, AbstractCallBack callback) {
		File file = new File(folder, METADATA_FILE_NAME);
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(md, writer);

			callback.log("Metadata created: " + file.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			callback.onError(e.toString());
		}
		return file.getAbsolutePath();
	}

	public static <T> BlocksMetaData genarateMetaData(long[] dims, long[] blockSize, long overlap,
			AbstractCallBack callback) {
		
		if ( overlap == 0 )
		{
			final Map<Integer, BlockInfo> blocks = divideIntoBlocks(blockSize, dims, callback);
			Config.setTotalBlocks(blocks.size());
	
			return new BlocksMetaData(blocks, blockSize, dims);
		}
		else
		{
			final Map<Integer, BlockInfo> blocks = generateBlocks(blockSize, dims, overlap, callback);
			Config.setTotalBlocks(blocks.size());
	
			return new BlocksMetaData(blocks, blockSize, dims);
		}
	}

	public static <T> BlocksMetaData genarateMetaData(DataPreview data, AbstractCallBack callback) {

		final long[] blockSize = data.getBlocksSizes();
		final long[] dims = data.getFile().getDimensions();
		final Map<Integer, BlockInfo> blocks = generateBlocks(blockSize, dims, data.getOverlap(), callback);
		Config.setTotalBlocks(blocks.size());

		return new BlocksMetaData(blocks, blockSize, dims);
	}

	public static <T> Map<Integer, BlockInfo> generateBlocks(long blockSize[], long[] dims, long overlap,
			AbstractCallBack callback) {
		final double[] sigmas = Util.getArrayFromValue((double) overlap, dims.length);
		final int[] halfKernelSizes = Gauss3.halfkernelsizes(sigmas);
		final long[] kernelSize = new long[dims.length];
		final long[] imgSize = new long[dims.length];
		for (int d = 0; d < dims.length; ++d) {
			kernelSize[d] = (overlap == 0) ? 0 : (halfKernelSizes[d] * 2 - 1);
			imgSize[d] = dims[d];
		}
	

		final Map<Integer, BlockInfo> blocks = divideIntoBlocks(blockSize, imgSize, kernelSize, callback);
		return blocks;
	}

	private static Map<Integer, BlockInfo> divideIntoBlocks(final long[] blockSize, final long[] imgSize,
			final long[] kernelSize, AbstractCallBack callback) {
	
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
				callback.log("Blocksize in dimension " + d + " (" + blockSize[d] + ") is smaller than the kernel ("
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

		MyLogger.log.info("imgSize " + Util.printCoordinates(imgSize));
		MyLogger.log.info("kernelSize " + Util.printCoordinates(kernelSize));
		MyLogger.log.info("blockSize " + Util.printCoordinates(blockSize));
		MyLogger.log.info("numBlocks " + Util.printCoordinates(numBlocks));
		MyLogger.log.info("effectiveSize of blocks" + Util.printCoordinates(effectiveSizeGeneral));
		MyLogger.log.info("effectiveLocalOffset " + Util.printCoordinates(effectiveLocalOffset));

		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator(numBlocks);
		final Map<Integer, BlockInfo> blockinfosList = new HashMap<Integer, BlockInfo>();

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
					new EffectiveBlockInfo(gridOffset, blockSize, effectiveSize,x1, x2, offset, effectiveLocalOffset, true));
			i++;
//			if (i % 10 == 0) {
				MyLogger.log.info(
						i+"- block " + Util.printCoordinates(gridOffset) + "size " + Util.printCoordinates(blockSize)
								+ "x1: " + Util.printCoordinates(x1) + " x2: " + Util.printCoordinates(x2) + " effectiveSize: "
								+ Util.printCoordinates(effectiveSize) + " offset: " + Util.printCoordinates(offset));
//			}
		}

		return blockinfosList;
	}

	private static Map<Integer, BlockInfo> divideIntoBlocks(final long[] blockSize, final long[] imgSize, AbstractCallBack callback) {
	
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
		final Map<Integer, BlockInfo> blockinfosList = new HashMap<Integer, BlockInfo>();

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

}
