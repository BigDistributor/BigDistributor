package net.preibisch.bigdistributor.algorithm.errorhandler.callback;


public interface AbstractCallBack {
	void onSuccess(int pos);
	void onError(String error);
	void log(String log);
	
	
}
