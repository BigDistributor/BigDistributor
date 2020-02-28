package net.preibisch.distribution.algorithm.blockmanagement.io;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.preibisch.distribution.algorithm.blockmanagement.block.Block;
import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.IOTools;

public class BlockFileManager {

	public static RandomAccessibleInterval<FloatType> mergeBlocksFiles(Map<Integer, Block> blockMap,
			File blocksFolder, DataExtension extension) {
		final Img<FloatType> resultImage = new CellImgFactory<FloatType>(64).create(new long[] { 0, 0, 0 },
				new FloatType());
		for (final Integer key : blockMap.keySet()) {
			File f = new File(blocksFolder, extension.file(key.toString()));
			RandomAccessibleInterval<FloatType> tmp = IOTools.openAs32Bit(f);
			blockMap.get(key).pasteBlock(resultImage, tmp);
		}
		return resultImage;
	}

	public static void saveAllBlocks(RandomAccessibleInterval<FloatType> image, List<Block> blocks,
			File folder, DataExtension extension) {
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);
		Integer i = 0;
		for (final Block block :blocks) {
			++i;
			File f = new File(folder, extension.file(i.toString()));
			saveOneBlock(infiniteImg, f, block);
		}
	}

	public static void saveOneBlock(RandomAccessible<FloatType> image, File file, Block block) {
		final Img<FloatType> tmp = ArrayImgs.floats(block.dims());
		block.copyBlock(image, tmp);
		IOTools.saveTiffStack(IOTools.getImagePlusInstance(tmp), file);
	}

	public static RandomAccessibleInterval<FloatType> getBlock(RandomAccessibleInterval<FloatType> image, Block block) {
		final Img<FloatType> tmp = ArrayImgs.floats(block.getBlockSize());
		final RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);
		block.copyBlock(infiniteImg, tmp);
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
}
