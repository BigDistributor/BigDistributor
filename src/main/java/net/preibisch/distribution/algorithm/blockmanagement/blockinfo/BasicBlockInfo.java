package net.preibisch.distribution.algorithm.blockmanagement.blockinfo;

import java.util.Arrays;

import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class BasicBlockInfo {

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
	
	public BoundingBox bb() {
		int[] minArray = Arrays.stream(min).mapToInt(i -> (int) i).toArray();
		int[] maxArray = Arrays.stream(min).mapToInt(i -> (int) i).toArray();
		return new BoundingBox(minArray, maxArray);
	}

	@Override
	public String toString() {
		return "BS:" + Util.printCoordinates(blockSize) + " GridOff:" + Util.printCoordinates(gridOffset) + " Eff_Size:"
				+ Util.printCoordinates(effectiveSize) + " min:" + Util.printCoordinates(min) + " max:"
				+ Util.printCoordinates(max);
	}

}
