package main.java.com.controllers.items.callback;


public interface AbstractCallBack {
	void onSuccess(int pos);
	void onError(String error);
	void log(String log);
	
	
}
