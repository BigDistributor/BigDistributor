package main.java.net.preibisch.distribution.algorithm.blockmanager.block;

public class EffectiveBlockInfo extends BasicBlockInfo implements BlockInfo {

	private long[] offset;
	private long[] effectiveLocalOffset;
	private boolean isPrecise;

	public EffectiveBlockInfo(long[] gridOffset, long[] blockSize, long[] effectiveSize, long[] min, long[] max,
			long[] offset, long[] effectiveLocalOffset, boolean isPrecise) {
		super(gridOffset, blockSize, effectiveSize, min, max);
		this.offset = offset;
		this.effectiveLocalOffset = effectiveLocalOffset;
		this.isPrecise = isPrecise;
	}

	public long[] getOffset() {
		return offset;
	}

	public long[] getEffectiveLocalOffset() {
		return effectiveLocalOffset;
	}

	public boolean isPrecise() {
		return isPrecise;
	}
}
