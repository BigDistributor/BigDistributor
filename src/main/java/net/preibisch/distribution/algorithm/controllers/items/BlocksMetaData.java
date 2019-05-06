package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.util.Map;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;

public class BlocksMetaData {
	private long[] dimensions;
	private long[] blocksize;
	private Map<Integer,BlockInfos> blocksInfo;
	

	public BlocksMetaData(Map<Integer,BlockInfos> blocksInfo,long[] bsizes, long[] dimensions) {
		super();
		this.dimensions = dimensions;
		this.blocksize = bsizes;
		this.blocksInfo = blocksInfo;
	}
	
	public Map<Integer, BlockInfos> getBlocksInfo() {
		return blocksInfo;
	}
	
	public void setBlocksInfo(Map<Integer, BlockInfos> blocksInfo) {
		this.blocksInfo = blocksInfo;
	}

	public long[] getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(long[] blocksize) {
		this.blocksize = blocksize;
	}

	public long[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(long[] dimensions) {
		this.dimensions = dimensions;
	}
	
	
	
	
}
