package net.preibisch.bigdistributor.algorithm.controllers.flow;

import net.preibisch.bigdistributor.algorithm.errorhandler.callback.AbstractCallBack;

public interface AbstractTask {
	
	public void start(int pos, AbstractCallBack callback)throws Exception;
}
