package net.preibisch.distribution.algorithm.clustering.workflow;

import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.FunctionSequenceManager;
import net.preibisch.distribution.algorithm.controllers.tasks.StatusListenerManager;
import net.preibisch.distribution.gui.items.ProgressBarPanel;

public class Workflow {
	
//	public static List<Block> blocks;
//	public static HashMap<Integer, Block> blockMap;
	public static ProgressBarPanel progressBarPanel;
//	public static AppMode appMode = AppMode.LOCAL_INPUT_MODE;

	public static void InitWorkflow() throws Exception {
		progressBarPanel = new ProgressBarPanel(0, 100);
		Workflow.runFunction(new StatusListenerManager());
	}

	public static void run(Flow flow) throws Exception {
		FunctionSequenceManager fl = new FunctionSequenceManager(flow);
		fl.start();
	}

	public static void runFunction(AbstractTask task) throws Exception {
		FunctionSequenceManager fl = new FunctionSequenceManager(task);
		fl.start();
	}

}
