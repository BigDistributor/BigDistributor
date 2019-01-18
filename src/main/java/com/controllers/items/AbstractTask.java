package main.java.com.controllers.items;

import main.java.com.controllers.items.callback.AbstractCallBack;

public interface AbstractTask {
	
	public void start(int pos,AbstractCallBack callback);
}
