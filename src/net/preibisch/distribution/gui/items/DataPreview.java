package net.preibisch.distribution.gui.items;

import java.util.ArrayList;
import java.util.Arrays;

import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.GraphicBlocksManager;
import net.preibisch.distribution.gui.GUIConfig;
import net.preibisch.distribution.io.img.ImgFile;

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

	public DataPreview(long[] dims,long overlap,long[] blockSize) {
		this(dims, overlap, blockSize, GUIConfig.PREVIEW_PREFERED_HEIGHT);
	}
	
	public DataPreview(long[] dims,long overlap,long[] blockSize,int previewPreferedHeight) {
		DataPreview.dims = dims;
		DataPreview.overlap = overlap;
		DataPreview.blocksSizes = blockSize;
		DataPreview.previewPreferedHeight = previewPreferedHeight;
		generateBlocks();
	}
	
	public static void fromFile(ImgFile file) {
		DataPreview.dims = file.getDims();
		DataPreview.previewPreferedHeight = GUIConfig.PREVIEW_PREFERED_HEIGHT;
		long[] blocksSizes = new long[dims.length];
		Arrays.fill(blocksSizes, BlockConfig.BLOCK_UNIT);
		DataPreview.blocksSizes = blocksSizes;
		System.out.println(DataPreview.str());
		generateBlocks();
	}
	
	public static String str() {	
		return "DataPreview: Dims = " +Util.printCoordinates(dims) + " | prefered height: "+previewPreferedHeight+ " | Block size: "+ blocksSizes ;
	}

}
