package main.java.net.preibisch.distribution.algorithm;

import java.util.Map;

import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;


public interface AbstractTask2 <T, R, K, V>  {

	public void start(T input, R output, Map<K, V> params, AbstractCallBack callback) throws Exception;
}
