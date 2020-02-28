//package net.preibisch.distribution.algorithm.controllers.flow;
//
//import net.preibisch.distribution.algorithm.controllers.flow.tasks.StatusListenerManager;
//import net.preibisch.distribution.gui.items.ProgressBarPanel;
//
//public class Workflow {
//	
////	public static List<Block> blocks;
////	public static HashMap<Integer, Block> blockMap;
//	public static ProgressBarPanel progressBarPanel;
////	public static AppMode appMode = AppMode.LOCAL_INPUT_MODE;
//
//	public static void InitWorkflow() throws Exception {
//		progressBarPanel = new ProgressBarPanel(0, 100);
//		Workflow.runFunction(new StatusListenerManager());
//	}
//
//	public static void run(Flow flow) throws Exception {
//		FunctionSequenceManager fl = new FunctionSequenceManager(flow);
//		fl.start();
//	}
//
//	public static void runFunction(AbstractTask task) throws Exception {
//		FunctionSequenceManager fl = new FunctionSequenceManager(task);
//		fl.start();
//	}
//
//}
