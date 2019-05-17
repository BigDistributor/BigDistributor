package main.java.net.preibisch.distribution.algorithm.blockmanager;

import java.util.Arrays;

import net.imglib2.util.Util;


public class BlockInfos {
	private long[] gridOffset;
	private long[] blockSize;
	private long[] offset;
	private long[] effectiveSize;
	private long[] x1;
	private long[] x2;
	private long[] effectiveLocalOffset;
	private boolean isPrecise ;
	
	public BlockInfos(long[] gridOffset,long[] blockSize,long[] x1,long[] x2 ) 
	{
		// TODO: 
	}
	
	public BlockInfos(long[] gridOffset,long[] blockSize, long[] offset, long[] effectiveSize, long[] x1,long[] x2,
			long[] effectiveLocalOffset, boolean isPrecise) {
		super();
		this.gridOffset = gridOffset;
		this.blockSize = blockSize;
		this.offset = offset;
		this.effectiveSize = effectiveSize;
		this.x1 = x1;
		this.x2 = x2;
		this.effectiveLocalOffset = effectiveLocalOffset;
		this.isPrecise = isPrecise;
	}

	public long[] getGridOffset() {
		return gridOffset;
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

public long[] getX1() {
	return x1;
}
public long[] getX2() {
	return x2;
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
				"GridOff:"+Util.printCoordinates(gridOffset)+
				" Offset:"+Arrays.toString(offset)+
				" Eff_Size:"+Arrays.toString(effectiveSize)+
				" X1:"+Arrays.toString(x1)+
				" X1:"+Arrays.toString(x2)+
				" Eff_local_off:"+Arrays.toString(effectiveLocalOffset)+
				" Pres: "+isPrecise; 
		return  str;
	}
	
}
