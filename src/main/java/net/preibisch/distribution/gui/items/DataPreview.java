package main.java.net.preibisch.distribution.gui.items;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.net.preibisch.distribution.algorithm.blockmanager.GraphicBlocksManager;
import main.java.net.preibisch.distribution.io.img.ImgFile;

public class DataPreview extends Object{
	//TODO use BlockInfos 
	private final ImgFile file;
	private long[] blocksSizes;
	private long overlap;
	private ArrayList<BlockPreview> blocksPreview;
	private int previewPreferedHeight ;

	private DataPreview(Builder builder) {
		this.file = builder.file;
		this.blocksPreview = builder.blocksPreview;
		this.overlap = builder.overlap;
		this.blocksSizes = builder.blocksSizes;
	}
	
	
	public void setBlocksPreview(ArrayList<BlockPreview> blocksPreview) {
		this.blocksPreview = blocksPreview;
	}
	 
	public ImgFile getFile() {
		return file;
	}
	
	public ArrayList<BlockPreview> getBlocksPreview() {
		return blocksPreview;
	}
	
	public long[] getBlocksSizes() {
		return blocksSizes;
	}
	public void setBlockSize(int position,long blockSize) {
		this.blocksSizes[position] = blockSize;
	}
	public void setOverlap(long overlap) {
		this.overlap = overlap;
	}
	
	public long getOverlap() {
		return overlap;
	}

	public int getPreviewPreferedHeight() {
		return previewPreferedHeight;
	}

	public void generateBlocks() {
		this.blocksPreview = GraphicBlocksManager.generateBlocks(file.getDims(), blocksSizes, overlap);
	}	
	
	public void setBlockSizes(long[] blocksSizes) {
		this.blocksSizes = blocksSizes;
	}
	
	private DataPreview(ImgFile file,long[] blocksSizes,long overlap) {
		this.file = file;
		this.overlap = overlap;
		this.blocksSizes = blocksSizes;
		
	}
	
	
	public static DataPreview of(ImgFile file, long[] blockSizes, long overlap) {
		return new DataPreview(file,blockSizes,overlap);
	}
	
	public static class Builder {
		private ImgFile file;
		private ArrayList<BlockPreview> blocksPreview;
		private final long DEFAULT_BLOCK_SIZE = 200;
		private long[] blocksSizes;
		private long overlap = 10 ;
		private int previewPreferedHeight ;
		
		public Builder BlocksPreview() {
			this.blocksPreview = blocksPreview;
			return this;
		}

		public Builder file(ImgFile file) {
			this.file = file;
			return this;
		}	
		
		public DataPreview build() {
//			previewPreferedHeight = (int) (numberBlocks[1]*blocksSize[1]);
			this.blocksSizes  = new long[file.getDims().length];
			Arrays.fill(this.blocksSizes , DEFAULT_BLOCK_SIZE);
			return new DataPreview(this);
		}
	}
}
