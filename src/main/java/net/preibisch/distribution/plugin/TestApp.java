package main.java.net.preibisch.distribution.plugin;

import java.util.Arrays;

import org.scijava.Context;
import org.scijava.log.LogService;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JDataFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.algorithm.controllers.tasks.StatusListenerManager;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.gui.items.LogFrame;
import main.java.net.preibisch.distribution.tools.config.Config;
import main.java.net.preibisch.distribution.tools.config.server.Account;
import main.java.net.preibisch.distribution.tools.config.server.ServerConfiguration;
import net.imagej.ops.OpService;

public class TestApp {
	
	//Define the mode of execution of the application
	private final static AppMode APP_MODE = AppMode.ClusterInputMode;
	
	//Identify the path of the task and the input file
	private final static String TASK_PATH = "/home/mzouink/Desktop/Task/GaussianTask.jar";
//	private final static String INPUT_PATH = "/home/mzouink/Desktop/Task/data/dataset.xml" ;
	private final static String INPUT_PATH = "/home/mzouink/Desktop/test/in.n5";
//	private final static String EXTRA_FILE_PATH = "" ;
	
	
	private static OpService opService;
	private static LogService logService;

	public static LogService getLogService() {
		return logService;
	}
	
	public static OpService getOpService() {
		return opService;
	}
	
	
	public static void main(String[] args) {
		new ImageJ();
		initLogger();
		initConfig();
		Job job = initJob();
		job.openTempFolder();
		Config.setJob(job);
		long blockSize = 80;
		final long[] blockSizes  = new long[job.getInput().getDimensions().length];
		Arrays.fill(blockSizes , blockSize);
		final long overlap = 10;
		
		DataPreview dataPreview =  DataPreview.of(job.getInput(),blockSizes,overlap);
		dataPreview.generateBlocks();
		Config.setDataPreview(dataPreview);
		
		new Workflow();
		Workflow.runFunction(new StatusListenerManager());
		
		Workflow.startWorkflow();
		
	}

	private static Job initJob() {
		JDataFile inputData = new JDataFile.Builder()
				.file(JFile.of(INPUT_PATH))
				.load()
				.getDataInfos()
				.build();
		
		return new Job.Builder()
			     .appMode(APP_MODE)
			     .task(JFile.of(TASK_PATH))
//			     .extra(JFile.of(EXTRA_FILE_PATH))
			     .input(inputData)
			     .createTempDir()
			     .buid();
	}

	private static void initConfig() {
		Account account = new Account.Builder().build();
		ServerConfiguration server = new ServerConfiguration.Builder().build();
		Login login = new Login.Builder().id().server(server).account(account).build();
		Config.setLogin(login);
	}

	private static void initLogger() {
		final Context context = new Context( OpService.class, LogService.class );
		opService = context.getService( OpService.class );
		logService = context.getService( LogService.class );
		LogFrame logFrame = new LogFrame(opService.getContext());
		MyLogger.SubLogger(logService);
		logFrame.setVisible(true);
	}

}


