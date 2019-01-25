package main.java.net.preibisch.distribution.algorithm.controllers.items.callback;

import java.util.Arrays;
import java.util.List;

import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.AllDataGetter;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.BlockCombinator;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.InputGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.InputSender;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.PreprocessTaskSender;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskBatchGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskBatchSender;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskLauncher;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskSender;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskShellGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.TaskShellSender;
import main.java.net.preibisch.distribution.tools.Config;

public final class FunctionSequenceManager {
	List<AbstractTask> functionsFlow ;
	public static AbstractCallBack callback;

	private void starterLocalInputModeWorkflow() {
		functionsFlow = Arrays.asList(new TaskSender(),
				new InputGenerator(),
					  new InputSender(),
					  new TaskShellGenerator(),
					 new TaskShellSender(),
					  new TaskBatchGenerator(),
					  new TaskBatchSender(),
					  new TaskLauncher()
					  );
		
	}

	private void starterClusterInputModeWorkflow() {
		functionsFlow = Arrays.asList(
					new MetaDataGenerator(),
					new TaskShellGenerator(),
					new TaskBatchGenerator(),
					new PreprocessTaskSender(),
					new TaskSender(),
					new TaskShellSender(),
					new TaskBatchSender(),
					new TaskLauncher()
					  );
	}
	
	
	
	private void resultClusterInputModeWorkflow() {
		functionsFlow = Arrays.asList(new AllDataGetter()
					  );
	}
	private void resultLocalInputModeWorkflow() {
		functionsFlow = Arrays.asList(new AllDataGetter(),
					  new BlockCombinator(),
					 new TaskShellSender(),
					  new TaskBatchGenerator(),
					  new TaskBatchSender(),
					  new TaskLauncher()
					  );
	}
	
	public FunctionSequenceManager(AbstractTask task) {
		functionsFlow = Arrays.asList(task);
		initCallBack();
	}
	
	public FunctionSequenceManager(AbstractTask... tasks) {
		functionsFlow = Arrays.asList(tasks);
		initCallBack();
	}
	
	 
	public FunctionSequenceManager(String task) {
		if(Workflow.START.equals(task)) {
		if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
			starterClusterInputModeWorkflow();
		} else if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
			starterLocalInputModeWorkflow();
		}}else if(Workflow.RESULT.equals(task))
		{
		if (AppMode.ClusterInputMode.equals(Config.getJob().getAppMode())) {
			resultClusterInputModeWorkflow();
		} else if (AppMode.LocalInputMode.equals(Config.getJob().getAppMode())) {
			resultLocalInputModeWorkflow();
		}
		}
		initCallBack();
	}

	private void initCallBack() {
		callback = new AbstractCallBack() {

			@Override
			public void onError(String error) {
				MyLogger.log.error(error);

			}

			@Override
			public void log(String log) {
				MyLogger.log.info(log);
			}

			@Override
			public void onSuccess(int pos) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
					if(functionsFlow.size()>(pos+1)) {
						functionsFlow.get(pos+1).start(pos+1, callback);
					}
					}
				});
				thread.start();

			}
		};
	}

	public void start() {
		functionsFlow.get(0).start(0, callback);	
	}
}
