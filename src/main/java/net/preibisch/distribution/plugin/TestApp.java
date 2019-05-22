package main.java.net.preibisch.distribution.plugin;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.headless.MainJob;
import main.java.net.preibisch.distribution.io.img.n5.N5IO;
import main.java.net.preibisch.distribution.tools.Tools;
import main.java.net.preibisch.distribution.tools.config.Config;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class TestApp {
	
	//Define the mode of execution of the application
	private final static AppMode mode = AppMode.RUN_LOCALLY;
	
	 //Identify the path of the task and the input file
	private final static String TMP_DIR = "/home/mzouink/Desktop/test2/";
	private final static String TASK_PATH = "/home/mzouink/Desktop/Task/GaussianTask.jar";
//	private final static String INPUT_PATH = "/home/mzouink/Desktop/Task/data/dataset.xml" ;
	private final static String INPUT_PATH  = "/home/mzouink/Desktop/Task/data/dataset.xml";
	private final static String OUT_FILE = "out.n5";
//	= "/home/mzouink/Desktop/test/in.n5";
//	private final static String EXTRA_FILE_PATH = "" ;
	
	
	

	public static void main(String[] args) throws IOException, JsonSyntaxException, JsonIOException, SpimDataException {
		new ImageJ();
		MyLogger.initLogger();
		
		if(mode != AppMode.RUN_LOCALLY) {
			Login.login();
		}

		Job job = Job.initJob(mode,TASK_PATH,INPUT_PATH,TMP_DIR);
//		job.openTempFolder();
		Config.setJob(job);
		RandomAccessibleInterval<FloatType> input = job.getInput().getLoader().fuse();
		ImageJFunctions.show(input,"Input");
		long blockSize = 80;
		final long[] blocksSizes  = Tools.fill(blockSize,job.getInput().getDimensions().length);
		final long overlap = 0;
		
//		DataPreview dataPreview =  DataPreview.of(job.getInput(),blockSizes,overlap);
//		dataPreview.generateBlocks();
		
		BlocksMetaData md = MetaDataGenerator.genarateMetaData(job.getInput().getDimensions(), blocksSizes, overlap, new Callback());
		int total = md.getBlocksInfo().size();
		System.out.println(md.toString());
		String metadataPath = MetaDataGenerator.createJSon(md, TMP_DIR, new Callback());
		
		String output = TMP_DIR+OUT_FILE;
		//Generate black result 
		MainJob.prestart(metadataPath, output);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i =0; i< 200; i+=20) {
			Runnable myTask = new MyTask(INPUT_PATH, metadataPath, output, i);
			executorService.submit(myTask);
			
		}
		

//		N5IO.show(output);
		
		
//				genarateMetaDataFile(dataPreview, TMP_DIR, new Callback());
//		System.out.println("Metadata = ");
//		Config.setDataPreview(dataPreview);
//		
//		Workflow.InitWorkflow();
//		Workflow.run(Flow.START_FLOW);
//		Workflow.runFunction(new StatusListenerManager());
//		
//		Workflow.startWorkflow();
		
	}
	
}
class MyTask implements Runnable{
	int id;
	private String input;
	private String metadata;
	private String output;
	
	public MyTask(String input, String metadata, String output,int id) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.metadata = metadata;
	}
	@Override
	public void run() {
		try {
			MainJob.blockTask(input, metadata, output, id);
//			N5IO.show(output);
		} catch (JsonSyntaxException | JsonIOException | SpimDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}


