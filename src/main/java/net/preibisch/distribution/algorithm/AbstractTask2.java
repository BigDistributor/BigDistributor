package net.preibisch.distribution.algorithm;

import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;


public interface AbstractTask2 <T,I, P>  {

	public T start(I input, P params, AbstractCallBack callback) throws Exception;
}
