package net.preibisch.distribution.algorithm.blockmanager.block;

import net.imglib2.util.Util;

public class BasicBlockInfo implements BlockInfo {

	private long[] gridOffset;
	private long[] blockSize;
	private long[] effectiveSize;
	private long[] min;
	private long[] max;

	public BasicBlockInfo(long[] gridOffset, long[] blockSize, long[] effectiveSize, long[] min, long[] max) {
		super();
		this.gridOffset = gridOffset;
		this.blockSize = blockSize;
		this.effectiveSize = effectiveSize;
		this.min = min;
		this.max = max;
	}

	public long[] getGridOffset() {
		return gridOffset;
	}

	public long[] getBlockSize() {
		return blockSize;
	}

	public long[] getEffectiveSize() {
		return effectiveSize;
	}

	public long[] getMax() {
		return max;
	}

	public long[] getMin() {
		return min;
	}

	@Override
	public String toString() {
		return "BS:" + Util.printCoordinates(blockSize) + " GridOff:" + Util.printCoordinates(gridOffset) + " Eff_Size:"
				+ Util.printCoordinates(effectiveSize) + " min:" + Util.printCoordinates(min) + " max:"
				+ Util.printCoordinates(max);
	}

}
