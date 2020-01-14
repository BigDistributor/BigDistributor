package net.preibisch.distribution.algorithm.controllers.items;

import mpicbg.spim.data.sequence.ViewId;

public class ViewIdMD {

	private int timepoint;
	private int setup;

	public ViewIdMD(int timePoint, int setup) {
		this.timepoint = timePoint;
		this.setup = setup;
	}

	public ViewIdMD(ViewId viewId) {
		this(viewId.getTimePointId(), viewId.getViewSetupId());
	}

	public int getSetup() {
		return setup;
	}

	public int getTimepoint() {
		return timepoint;
	}
	
	public ViewId toViewId() {
		return new ViewId(timepoint, setup);
	}
}
