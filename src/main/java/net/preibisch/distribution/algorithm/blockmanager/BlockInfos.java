package main.java.net.preibisch.distribution.algorithm.blockmanager;

import java.util.Arrays;

import net.imglib2.util.Util;


public class BlockInfos {
	private long[] blockSize;
	private long[] offset;
	private long[] effectiveSize;
	private long[] effectiveOffset;
	private long[] effectiveLocalOffset;
	private boolean isPrecise ;
	
	
	public BlockInfos(long[] blockSize, long[] offset, long[] effectiveSize, long[] effectiveOffset,
			long[] effectiveLocalOffset, boolean isPrecise) {
		super();
		this.blockSize = blockSize;
		this.offset = offset;
		this.effectiveSize = effectiveSize;
		this.effectiveOffset = effectiveOffset;
		this.effectiveLocalOffset = effectiveLocalOffset;
		this.isPrecise = isPrecise;
	}


	public long[] getBlockSize() {
		return blockSize;
	}


	public long[] getOffset() {
		return offset;
	}


	public long[] getEffectiveSize() {
		return effectiveSize;
	}


	public long[] getEffectiveOffset() {
		return effectiveOffset;
	}


	public long[] getEffectiveLocalOffset() {
		return effectiveLocalOffset;
	}


	public boolean isPrecise() {
		return isPrecise;
	}
	
	
	@Override
	public String toString() {
		String str = "BS:"+Util.printCoordinates(blockSize)+
				" Offset:"+Arrays.toString(offset)+
				" Eff_Size:"+Arrays.toString(effectiveSize)+
				" Eff_Off:"+Arrays.toString(effectiveOffset)+
				" Eff_local_off:"+Arrays.toString(effectiveLocalOffset)+
				" Pres: "+isPrecise; 
		return  str;
	}
	
}
