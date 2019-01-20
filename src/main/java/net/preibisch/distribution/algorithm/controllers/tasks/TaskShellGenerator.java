package main.java.net.preibisch.distribution.algorithm.controllers.tasks;

import main.java.net.preibisch.distribution.algorithm.clustering.scripting.ShellGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class TaskShellGenerator implements AbstractTask {

	@Override
	public void start(int pos, AbstractCallBack callback) {
		ShellGenerator.generateTaskShell(pos,callback);
	}

}
