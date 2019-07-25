package net.preibisch.distribution.algorithm.blockmanager.block;

public class ComplexBlockInfo extends BasicBlockInfo implements BlockInfo {

	private long[] offset;
	private long[] effectiveLocalOffset;
	private long[] EffectiveOffset;
	private boolean isPrecise;

	public ComplexBlockInfo(long[] gridOffset, long[] blockSize, long[] effectiveSize, long[] min, long[] max,
			long[] offset, long[] EffectiveOffset,long[] effectiveLocalOffset, boolean isPrecise) {
		super(gridOffset, blockSize, effectiveSize, min, max);
		this.offset = offset;
		this.EffectiveOffset = EffectiveOffset;
		this.effectiveLocalOffset = effectiveLocalOffset;
		this.isPrecise = isPrecise;
	}

	public long[] getOffset() {
		return offset;
	}

	public long[] getEffectiveLocalOffset() {
		return effectiveLocalOffset;
	}

	public long[] getEffectiveOffset() {
		return EffectiveOffset;
	}

	public boolean isPrecise() {
		return isPrecise;
	}
}
