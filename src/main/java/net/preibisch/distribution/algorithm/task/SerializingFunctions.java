package net.preibisch.distribution.algorithm.task;

import java.util.ArrayList;
import java.util.List;

import mpicbg.spim.data.sequence.ViewId;
import net.preibisch.distribution.algorithm.controllers.items.ViewIdMD;

public class SerializingFunctions {
	public List<ViewIdMD> setViewIds(List<ViewId> viewIds) {
		List<ViewIdMD> viewIdMD = new ArrayList<>();
		for (ViewId viewid : viewIds) {
			viewIdMD.add(new ViewIdMD(viewid));
		}
		return viewIdMD;
	}
	
	public List<ViewId> getViewIds(List<ViewIdMD> viewIdsmd) {
		List<ViewId> viewIds = new ArrayList<>();
		for (ViewIdMD viewid : viewIdsmd) {
			viewIds.add(new ViewId(viewid.getTimepoint(), viewid.getSetup()));
		}
		return viewIds;
	}
}
