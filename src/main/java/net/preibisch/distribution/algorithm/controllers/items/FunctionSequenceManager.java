package net.preibisch.distribution.algorithm.controllers.items;

import java.util.Arrays;
import java.util.List;

import net.preibisch.distribution.algorithm.clustering.workflow.Flow;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGeneratorTask;
import net.preibisch.distribution.algorithm.controllers.tasks.AllDataGetter;
import net.preibisch.distribution.algorithm.controllers.tasks.BlockCombinator;
import net.preibisch.distribution.algorithm.controllers.tasks.InputGenerator;
import net.preibisch.distribution.algorithm.controllers.tasks.InputSender;
import net.preibisch.distribution.algorithm.controllers.tasks.TaskBatchGenerator;
import net.preibisch.distribution.algorithm.controllers.tasks.TaskShellSender;

public final class FunctionSequenceManager {
	List<AbstractTask> functionsFlow ;
	public static AbstractCallBack callback;

	private void starterLocalInputModeWorkflow() {
		functionsFlow = Arrays.asList(
				new InputGenerator(),
					  new InputSender(),
					 new TaskShellSender(),
					  new TaskBatchGenerator()
					  );
		
	}

	private void starterClusterInputModeWorkflow() {
		functionsFlow = Arrays.asList(
					new MetadataGeneratorTask(),
					new TaskBatchGenerator(),
					new TaskShellSender()
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
					  new TaskBatchGenerator()
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
	
	 
	public FunctionSequenceManager(Flow flow) {
		AppMode mode = Job.getAppMode();
		if(Flow.START_FLOW.equals(flow)) {
		if (AppMode.CLUSTER_INPUT_MODE.equals(mode)) {
			starterClusterInputModeWorkflow();
		} else if (AppMode.LOCAL_INPUT_MODE.equals(mode)) {
			starterLocalInputModeWorkflow();
		}}else if(Flow.RESULT_FLOW.equals(flow))
		{
		if (AppMode.CLUSTER_INPUT_MODE.equals(mode)) {
			resultClusterInputModeWorkflow();
		} else if (AppMode.LOCAL_INPUT_MODE.equals(mode)) {
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
						try {
							functionsFlow.get(pos+1).start(pos+1, callback);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
				});
				thread.start();

			}
		};
	}

	public void start() throws Exception {
		functionsFlow.get(0).start(0, callback);	
	}
}
