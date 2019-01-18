package main.java.com.controllers.tasks;

import main.java.com.clustering.scripting.ShellGenerator;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.callback.AbstractCallBack;

public class TaskShellGenerator implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		ShellGenerator.generateTaskShell(pos,callback);
	}

}
