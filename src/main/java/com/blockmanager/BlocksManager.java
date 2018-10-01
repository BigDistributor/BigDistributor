package main.java.com.blockmanager;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.google.common.io.Files;

import main.java.com.clustering.MyCallBack;
import main.java.com.gui.items.BlockView;
import main.java.com.gui.items.Colors;
import main.java.com.multithreading.Threads;
import main.java.com.tools.Config;
import main.java.com.tools.Helper;
import main.java.com.tools.IOFunctions;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class BlocksManager {
	public static void generateResult(HashMap<Integer, Block> blockMap, String blocksDir,MyCallBack callback) {
		Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getOriginalInputFilePath()));
		final Img<FloatType> resultImage = new CellImgFactory<FloatType>(64).create(Helper.getDimensions(image),
				new FloatType());
		for (final Integer key : blockMap.keySet()) {
			String string = blocksDir + "/" + key + ".tif";
			Img<FloatType> tmp = IOFunctions.openAs32Bit(new File(string));
			blockMap.get(key).pasteBlock(resultImage, tmp, callback);
		}
		ImageJFunctions.show(resultImage).setTitle("Result");
	}

	public static HashMap<Integer, Block> saveBlocks(Img<FloatType> image, List<Block> blocks,MyCallBack callBack) {
		final long[] blockSizeDim = Config.getBlocksSize();
		final Img<FloatType> tmp = ArrayImgs.floats(blockSizeDim);
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);
		int i = 0;
		final HashMap<Integer, Block> blockMap = new HashMap<>();
		File tempDir = Files.createTempDir();
		Config.setTempFolderPath(tempDir.getAbsolutePath());
		callBack.log("Temp Dir: " + tempDir.getAbsolutePath());
		for (final Block block : blocks) {
			++i;
			block.copyBlock(infiniteImg, tmp, callBack);
			blockMap.put(i, block);
			IOFunctions.saveTiffStack(IOFunctions.getImagePlusInstance(tmp),
					tempDir.getAbsolutePath() + "/" + i + ".tif", callBack);
//			Todo
//			Config.progressValue = (i * 100) / blocks.size();
		}
		return blockMap;
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

	public static <T> List<Block> generateBlocks(RandomAccessibleInterval<T> input, long[] blockSize, int sigma,MyCallBack callback) {
		Config.setBlocksSize(blockSize);
		final ExecutorService service = Threads.createExService(1);
		final BlockGenerator<Block> generator = new BlockGeneratorFixedSizePrecise(service, blockSize);
		final double[] sigmas = Util.getArrayFromValue((double) Config.getOverlap(), input.numDimensions());
		final int[] halfKernelSizes = Gauss3.halfkernelsizes(sigmas);
		final long[] kernelSize = new long[input.numDimensions()];
		final long[] imgSize = new long[input.numDimensions()];
		for (int d = 0; d < input.numDimensions(); ++d) {
			kernelSize[d] = halfKernelSizes[d] * 2 - 1;
			imgSize[d] = input.dimension(d);
		}
		final List<Block> blocks = generator.divideIntoBlocks(imgSize, kernelSize, callback);
		return blocks;
	}

	public static ArrayList<BlockView> getBlocks(long[] dimensions, long[] numberBlocks, long[] blocksDimensions,
			int overlap, double perspectiveRation) {

		int[] previewBlockDimensions = new int[blocksDimensions.length];
		for (int i = 0; i < previewBlockDimensions.length; i++)
			previewBlockDimensions[i] = (int) (blocksDimensions[i] * perspectiveRation);

		int[] previewDimensions = new int[dimensions.length];
		for (int i = 0; i < previewDimensions.length; i++)
			previewDimensions[i] = (int) (dimensions[i] * perspectiveRation);

		ArrayList<BlockView> blocks = new ArrayList<BlockView>();
		int lastBlockXSize = (int) ((previewDimensions[0] % previewBlockDimensions[0]) > 0
				? previewDimensions[0] % previewBlockDimensions[0]
				: previewBlockDimensions[0]);
		int lastBlockYSize = (int) ((previewDimensions[1] % previewBlockDimensions[1]) > 0
				? (previewDimensions[1] % previewBlockDimensions[1])
				: previewBlockDimensions[1]);

		for (int i = 0; i < numberBlocks[1]; i++) {
			for (int j = 0; j < numberBlocks[0]; j++) {
				if (i < numberBlocks[1] - 1) {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockView(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap,
										previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										previewBlockDimensions[1]),
								Colors.START));
					} else {
						blocks.add(new BlockView(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										lastBlockXSize + 2 * overlap, previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], lastBlockXSize,
										previewBlockDimensions[1]),
								Colors.START));
					}
				} else {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockView(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap, lastBlockYSize + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										lastBlockYSize),
								Colors.START));
					} else {
						blocks.add(new BlockView(
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

	public static ArrayList<BlockView> getBlocks(long[] graphicBlocks, int computeSizePreviewBox) {
		ArrayList<BlockView> blocks = new ArrayList<BlockView>();
		int i;
		for (i = 0; i < graphicBlocks[0]; i++) {
			for (int j = 0; j < graphicBlocks[1]; j++) {
				blocks.add(new BlockView(null, new Rectangle(j * computeSizePreviewBox, i * computeSizePreviewBox,
						computeSizePreviewBox, computeSizePreviewBox), Colors.START));
			}
		}
		for (int k = 0; k < graphicBlocks[2]; k++) {
			blocks.add(new BlockView(null, new Rectangle(k * computeSizePreviewBox, i * computeSizePreviewBox,
					computeSizePreviewBox, computeSizePreviewBox), Colors.START));
		}
		return blocks;
	}
}