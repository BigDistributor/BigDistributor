package main.java.net.preibisch.distribution.gui.items;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import main.java.net.preibisch.distribution.algorithm.blockmanager.GraphicBlocksManager;
import main.java.net.preibisch.distribution.gui.GUIConfig;
import main.java.net.preibisch.distribution.io.img.ImgFile;

public class DataPreview extends Object {
	private static long[] dims;
	private static long[] blocksSizes;
	private static long overlap = 10;
	private static ArrayList<BlockPreview> blocksPreview;
	private static int previewPreferedHeight ;

	public static long[] getDims() {
		return dims;
	}
	
	public static ArrayList<BlockPreview> getBlocksPreview() {
		return blocksPreview;
	}

	public static long[] getBlocksSizes() {
		return blocksSizes;
	}

	public static void setBlockSize(int position, long blockSize) {
		DataPreview.blocksSizes[position] = blockSize;
	}

	public static void setOverlap(long overlap) {
		DataPreview.overlap = overlap;
	}

	public static long getOverlap() {
		return overlap;
	}

	public static int getPreviewPreferedHeight() {
		return previewPreferedHeight;
	}

	public static void generateBlocks() {
		DataPreview.blocksPreview = GraphicBlocksManager.generateBlocks(dims, blocksSizes, overlap);
	}

	public static void fromFile(ImgFile file) {
		DataPreview.dims = file.getDims();
		DataPreview.previewPreferedHeight = GUIConfig.PREVIEW_PREFERED_HEIGHT;
		long[] blocksSizes = new long[dims.length];
		Arrays.fill(blocksSizes, BlockConfig.BLOCK_UNIT);
		DataPreview.blocksSizes = blocksSizes;
		generateBlocks();
	}

}
