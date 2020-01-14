package net.preibisch.distribution.algorithm.blockmanager;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.multithreading.Threads;
import net.preibisch.distribution.gui.items.BlockPreview;
import net.preibisch.distribution.gui.items.Colors;
import net.preibisch.distribution.gui.items.DataPreview;
import net.preibisch.distribution.io.IOTools;

public class BlocksManager {
	public static Img<FloatType> generateResult(HashMap<Integer, Block> blockMap, String blocksDir,
			AbstractCallBack callback) {

		final Img<FloatType> resultImage = new CellImgFactory<FloatType>(64)
				.create(new long[] {0,0,0}, new FloatType());
//		Job.getInput().getDims()
		for (final Integer key : blockMap.keySet()) {
			String string = blocksDir + "/" + key + ".tif";
			Img<FloatType> tmp = IOTools.openAs32Bit(new File(string));
			blockMap.get(key).pasteBlock(resultImage, tmp, callback);
		}
		return resultImage;
	}
	
	

	public static void saveBlocks(RandomAccessibleInterval<FloatType> image, long[] blockSize, List<Block> blocks,
			AbstractCallBack callBack) {
		final long[] blockSizeDim = blockSize;
		final Img<FloatType> tmp = ArrayImgs.floats(blockSizeDim);
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);
		int i = 0;
		String tempDir = Job.getTmpDir().getAbsolutePath();
		callBack.log("Temp Dir: " + tempDir);
		for (final Block block : blocks) {
			++i;
			block.copyBlock(infiniteImg, tmp, callBack);
			IOTools.saveTiffStack(IOTools.getImagePlusInstance(tmp), tempDir + "/" + i + ".tif", callBack);
		}
	}

	public static void saveBlock(RandomAccessibleInterval<FloatType> image, long[] blockSize, String path, int id, Block block,
			AbstractCallBack callBack) {

		final long[] blockSizeDim = blockSize;
		final Img<FloatType> tmp = ArrayImgs.floats(blockSizeDim);
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);

		block.copyBlock(infiniteImg, tmp, callBack);

		IOTools.saveTiffStack(IOTools.getImagePlusInstance(tmp), new File(path , id + ".tif").getAbsolutePath(), callBack);

	}
	
	
	public static RandomAccessibleInterval<FloatType> getBlock(RandomAccessibleInterval<FloatType> image, Block block,
			AbstractCallBack callBack) {
		
		final Img<FloatType> tmp = ArrayImgs.floats(block.getBlockSize());
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);

		block.copyBlock(infiniteImg, tmp, callBack);
		return tmp;

	}

	public static double imageDifference(final RandomAccessibleInterval<FloatType> img1,
			final RandomAccessibleInterval<FloatType> img2) {
		final Cursor<FloatType> c1 = Views.iterable(img1).localizingCursor();
		final RandomAccess<FloatType> r2 = img2.randomAccess();
		double sumChange = 0;
		while (c1.hasNext()) {
			c1.fwd();
			r2.setPosition(c1);
			sumChange += Math.abs(c1.get().get() - r2.get().get());
		}
		return sumChange;
	}

	public static <T> List<Block> generateBlocks(DataPreview data, AbstractCallBack callback) {
		final ExecutorService service = Threads.createExService(1);
		long[] dims = data.getDims();
		final BlockGenerator<Block> generator = new BlockGeneratorFixedSizePrecise(service, data.getBlocksSizes());
		final double[] sigmas = Util.getArrayFromValue((double) data.getOverlap(), dims.length);
		final int[] halfKernelSizes = Gauss3.halfkernelsizes(sigmas);
		final long[] kernelSize = new long[dims.length];
		final long[] imgSize = new long[dims.length];
		for (int d = 0; d < dims.length; ++d) {
			kernelSize[d] = halfKernelSizes[d] * 2 - 1;
			imgSize[d] = dims[d];
		}
		final List<Block> blocks = generator.divideIntoBlocks(imgSize, kernelSize, callback);
		return blocks;
	}

	public static ArrayList<BlockPreview> getBlocks(long[] dimensions, long[] numberBlocks, long[] blocksDimensions,
			int overlap, double perspectiveRation) {

		int[] previewBlockDimensions = new int[blocksDimensions.length];
		for (int i = 0; i < previewBlockDimensions.length; i++)
			previewBlockDimensions[i] = (int) (blocksDimensions[i] * perspectiveRation);

		int[] previewDimensions = new int[dimensions.length];
		for (int i = 0; i < previewDimensions.length; i++)
			previewDimensions[i] = (int) (dimensions[i] * perspectiveRation);

		ArrayList<BlockPreview> blocks = new ArrayList<BlockPreview>();
		int lastBlockXSize = (previewDimensions[0] % previewBlockDimensions[0]) > 0
				? previewDimensions[0] % previewBlockDimensions[0]
				: previewBlockDimensions[0];
		int lastBlockYSize = (previewDimensions[1] % previewBlockDimensions[1]) > 0
				? (previewDimensions[1] % previewBlockDimensions[1])
				: previewBlockDimensions[1];

		for (int i = 0; i < numberBlocks[1]; i++) {
			for (int j = 0; j < numberBlocks[0]; j++) {
				if (i < numberBlocks[1] - 1) {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap,
										previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										previewBlockDimensions[1]),
								Colors.START));
					} else {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										lastBlockXSize + 2 * overlap, previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], lastBlockXSize,
										previewBlockDimensions[1]),
								Colors.START));
					}
				} else {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap, lastBlockYSize + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										lastBlockYSize),
								Colors.START));
					} else {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										lastBlockXSize + 2 * overlap, lastBlockYSize + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], lastBlockXSize, lastBlockYSize),
								Colors.START));
					}
				}
			}
		}
		return blocks;
	}

	public static ArrayList<BlockPreview> getBlocks(long[] graphicBlocks, int computeSizePreviewBox) {
		ArrayList<BlockPreview> blocks = new ArrayList<BlockPreview>();
		int i;
		for (i = 0; i < graphicBlocks[0]; i++) {
			for (int j = 0; j < graphicBlocks[1]; j++) {
				blocks.add(new BlockPreview(null, new Rectangle(j * computeSizePreviewBox, i * computeSizePreviewBox,
						computeSizePreviewBox, computeSizePreviewBox), Colors.START));
			}
		}
		for (int k = 0; k < graphicBlocks[2]; k++) {
			blocks.add(new BlockPreview(null, new Rectangle(k * computeSizePreviewBox, i * computeSizePreviewBox,
					computeSizePreviewBox, computeSizePreviewBox), Colors.START));
		}
		return blocks;
	}
}
