package net.preibisch.distribution.algorithm.controllers.flow;

import net.preibisch.distribution.algorithm.errorhandler.callback.AbstractCallBack;

public interface AbstractTask {
	
	public void start(int pos,AbstractCallBack callback)throws Exception;
}
