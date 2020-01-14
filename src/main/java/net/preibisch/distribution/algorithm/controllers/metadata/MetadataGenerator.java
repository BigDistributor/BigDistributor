package net.preibisch.distribution.algorithm.controllers.metadata;

import java.util.Map;

import net.imglib2.Interval;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockGenerator;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class MetadataGenerator {
		
	public static <T> BlocksMetaData genarateMetaData(long[] dims, long[] blockSize, long overlap,
			AbstractCallBack callback) throws Exception {
		
		if ( overlap == 0 )
		{
			final Map<Integer, BasicBlockInfo> blocks = BasicBlockGenerator.divideIntoBlocks(blockSize, dims, callback);
			return new BlocksMetaData(blocks, blockSize, dims,blocks.size());
		}
		else
		{
			throw new Exception("TODO ! ");
//			final Map<Integer, BlockInfo> blocks = ComplexBloxGenerator.generateBlocks(blockSize, dims, overlap, callback);	
//			return new BlocksMetaData(blocks, blockSize, dims,blocks.size());
		}
	}
	
	public static <T> Map<Integer, BasicBlockInfo> generateBlocks(Interval bb, int[] blockSize) {
		return generateBlocks(bb,Util.int2long(blockSize), 0);
	}
	
	
	public static <T> Map<Integer, BasicBlockInfo> generateBlocks(Interval bb, long[] blockSize, int overlap) {
		
		if ( overlap == 0 )
		{
			final Map<Integer, BasicBlockInfo> blocks = BasicBlockGenerator.divideIntoBlocks(bb, blockSize);
			return blocks;
		}
		else
		{
			System.out.println("TODO");
			return null;
		}
		
	}

}
