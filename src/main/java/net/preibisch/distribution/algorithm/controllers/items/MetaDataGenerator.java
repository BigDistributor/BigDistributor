package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.tools.Config;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.util.Util;

public class MetaDataGenerator implements AbstractTask {
	private final static String METADATA_FILE_NAME = "METADATA.json";

	@Override
	public void start(int pos, AbstractCallBack callback) {
		callback.log("Creating metadata..");
		BlocksMetaData md = new BlocksMetaData(generateBlocks(callback));
		Config.getJob().setMetaDataPath(createJSon(md,Config.getJob().getTmpDir(), callback));
		callback.onSuccess(pos);
	}

	private static String createJSon(BlocksMetaData md, String folder, AbstractCallBack callback) {
		File file = new File(folder,METADATA_FILE_NAME);
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(md, writer);
			
			callback.log("Metadata created: "+file.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			callback.onError(e.toString());
		}
		return file.getAbsolutePath();
	}

	private static <T> List<BlockInfos> generateBlocks(AbstractCallBack callback) {

		final DataPreview data = Config.getDataPreview();
		final long[] blockSize = data.getBlocksSizes();
		final long[] dims = data.getFile().getDimensions();
		final double[] sigmas = Util.getArrayFromValue((double) data.getOverlap(), dims.length);
		final int[] halfKernelSizes = Gauss3.halfkernelsizes(sigmas);
		final long[] kernelSize = new long[dims.length];
		final long[] imgSize = new long[dims.length];
		for (int d = 0; d < dims.length; ++d) {
			kernelSize[d] = halfKernelSizes[d] * 2 - 1;
			imgSize[d] = dims[d];
		}

		final List<BlockInfos> blocks = divideIntoBlocks(blockSize, imgSize, kernelSize, callback);
		Config.setTotalInputFiles(blocks.size());
		return blocks;
	}

	private static ArrayList<BlockInfos> divideIntoBlocks(final long[] blockSize, final long[] imgSize,
			final long[] kernelSize, AbstractCallBack callback) {
		final int numDimensions = imgSize.length;

		// compute the effective size & local offset of each block
		// this is the same for all blocks
		final long[] effectiveSizeGeneral = new long[numDimensions];
		final long[] effectiveLocalOffset = new long[numDimensions];

		for (int d = 0; d < numDimensions; ++d) {
			effectiveSizeGeneral[d] = blockSize[d] - kernelSize[d] + 1;

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

		System.out.println("imgSize " + Util.printCoordinates(imgSize));
		System.out.println("kernelSize " + Util.printCoordinates(kernelSize));
		System.out.println("blockSize " + Util.printCoordinates(blockSize));
		System.out.println("numBlocks " + Util.printCoordinates(numBlocks));
		System.out.println("effectiveSize of blocks" + Util.printCoordinates(effectiveSizeGeneral));
		System.out.println("effectiveLocalOffset " + Util.printCoordinates(effectiveLocalOffset));

		// now we instantiate the individual blocks iterating over all dimensions
		// we use the well-known ArrayLocalizableCursor for that
		final LocalizingZeroMinIntervalIterator cursor = new LocalizingZeroMinIntervalIterator(numBlocks);
		final ArrayList<BlockInfos> blockinfosList = new ArrayList<BlockInfos>();

		final int[] currentBlock = new int[numDimensions];

		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(currentBlock);

			// compute the current offset
			final long[] offset = new long[numDimensions];
			final long[] effectiveOffset = new long[numDimensions];
			final long[] effectiveSize = effectiveSizeGeneral.clone();

			for (int d = 0; d < numDimensions; ++d) {
				effectiveOffset[d] = currentBlock[d] * effectiveSize[d];
				offset[d] = effectiveOffset[d] - kernelSize[d] / 2;

				if (effectiveOffset[d] + effectiveSize[d] > imgSize[d])
					effectiveSize[d] = imgSize[d] - effectiveOffset[d];
			}

			blockinfosList
					.add(new BlockInfos(blockSize, offset, effectiveSize, effectiveOffset, effectiveLocalOffset, true));
			// System.out.println( "block " + Util.printCoordinates( currentBlock ) + "
			// effectiveOffset: " + Util.printCoordinates( effectiveOffset ) + "
			// effectiveSize: " + Util.printCoordinates( effectiveSize ) + " offset: " +
			// Util.printCoordinates( offset ) + " inside: " + inside );
		}

		return blockinfosList;
	}

}
