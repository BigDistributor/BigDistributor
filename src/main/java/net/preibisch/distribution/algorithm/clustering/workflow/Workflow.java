package main.java.net.preibisch.distribution.algorithm.clustering.workflow;

import java.io.IOException;

import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.FunctionSequenceManager;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.StatusListenerManager;
import main.java.net.preibisch.distribution.gui.items.ProgressBarPanel;

public class Workflow {
	
//	public static List<Block> blocks;
//	public static HashMap<Integer, Block> blockMap;
	public static ProgressBarPanel progressBarPanel;
//	public static AppMode appMode = AppMode.LOCAL_INPUT_MODE;

	public static void InitWorkflow() throws IOException {
		progressBarPanel = new ProgressBarPanel(0, 100);
		Workflow.runFunction(new StatusListenerManager());
	}

	public static void run(Flow flow) throws IOException {
		FunctionSequenceManager fl = new FunctionSequenceManager(flow);
		fl.start();
	}

	public static void runFunction(AbstractTask task) throws IOException {
		FunctionSequenceManager fl = new FunctionSequenceManager(task);
		fl.start();
	}

}
