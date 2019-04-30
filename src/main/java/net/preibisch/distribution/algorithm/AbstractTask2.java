package main.java.net.preibisch.distribution.algorithm;

import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;


public interface AbstractTask2 <I, O, P>  {

	public void start(I input, O output, P params, AbstractCallBack callback) throws Exception;
}
