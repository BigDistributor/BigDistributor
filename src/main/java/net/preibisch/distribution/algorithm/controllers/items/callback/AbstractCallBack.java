package net.preibisch.distribution.algorithm.controllers.items.callback;


public interface AbstractCallBack {
	void onSuccess(int pos);
	void onError(String error);
	void log(String log);
	
	
}
