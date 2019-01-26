package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.util.Map;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;

public class BlocksMetaData {
	private Map<Integer,BlockInfos> blocksInfo;

	public BlocksMetaData(Map<Integer,BlockInfos> blocksInfo) {
		super();
		this.blocksInfo = blocksInfo;
	}
	
	public Map<Integer, BlockInfos> get() {
		return blocksInfo;
	}
	
	public void set(Map<Integer, BlockInfos> blocksInfo) {
		this.blocksInfo = blocksInfo;
	}
	
	
	
	
}
