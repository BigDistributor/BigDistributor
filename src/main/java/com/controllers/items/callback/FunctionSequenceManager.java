package main.java.com.controllers.items.callback;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import main.java.com.clustering.workflow.MyLogger;
import main.java.com.clustering.workflow.Workflow;
import main.java.com.controllers.items.AbstractTask;
import main.java.com.controllers.items.AppMode;
import main.java.com.controllers.tasks.AllDataGetter;
import main.java.com.controllers.tasks.BlockCombinator;
import main.java.com.controllers.tasks.InputGenerator;
import main.java.com.controllers.tasks.InputSender;
import main.java.com.controllers.tasks.TaskBatchGenerator;
import main.java.com.controllers.tasks.TaskBatchSender;
import main.java.com.controllers.tasks.TaskLauncher;
import main.java.com.controllers.tasks.TaskSender;
import main.java.com.controllers.tasks.TaskShellGenerator;
import main.java.com.controllers.tasks.TaskShellSender;
import main.java.com.tools.Config;

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
		functionsFlow = Arrays.asList(new TaskSender(),
					  new TaskShellGenerator(),
					 new TaskShellSender(),
					  new TaskBatchGenerator(),
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
