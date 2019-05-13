package main.java.net.preibisch.distribution.headless;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.AbstractTask2;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.io.img.n5.LoadN5;
import main.java.net.preibisch.distribution.io.img.n5.N5IO;
import main.java.net.preibisch.distribution.taskexample.Fusion;
import main.java.net.preibisch.distribution.tools.Tools;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MainJob implements Callable<Void> {

	@Option(names = { "-t", "--task" }, required = false, description = "pre : to generate black result | job : to run the task")
	private String job;
	
	@Option(names = { "-out", "--output" }, required = false, description = "The path of the Data")
	private String output;
	
	@Option(names = { "-in", "--input" }, required = false, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = false, description = "The path of the MetaData file")
	private String metadataPath;
	
	@Option(names = { "-id" }, required = false, description = "The id of block")
	private Integer id;

	private AbstractTask2<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<FloatType>, Object>  task;
	
	
	public static void main(String[] args) throws Exception {
//		String[] ar = testArgs();
		CommandLine.call(new MainJob(), args);
		show();
		
	}
	
	private static String[] testArgs() throws Exception {
//		String in = "/home/mzouink/Desktop/test/in.n5";
		String in = "/home/mzouink/Desktop/Task/data/dataset.xml";
		String out = "/home/mzouink/Desktop/test/out.n5";
		String meta = "/home/mzouink/Desktop/test/METADATA.json";;
		int id = 1;
		String job = "job";
		String args = "-t "+job+" -in "+in+" -out "+out+" -m "+meta+" -id "+id;

		System.out.println(args);
		return args.split(" ");
	}
	
	private static void show() {
		String in = "/home/mzouink/Desktop/test/in.n5";

		String out = "/home/mzouink/Desktop/test/out.n5";
		
		new ImageJ();
		ImageJFunctions.show(new LoadN5(in).fuse(),"in");

		ImageJFunctions.show(new LoadN5(out).fuse(),"out");
		
	}
	

	public MainJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void call() throws Exception {
		switch (job) {
		case "pre":
			System.out.println("Pre process task..");
			prestart(metadataPath, output);
			break;
		case "job":
			System.out.println("Job..");
			blockTask(input,metadataPath,output,id);
			break;

		default:
			System.out.println("Error task invalide");
			break;
		}
		return null;
		
		
	}


	public static void blockTask(String input, String metadataPath, String output, int id) throws SpimDataException, JsonSyntaxException, JsonIOException, IOException {
		System.out.println(id +"- Start");
		
		BlocksMetaData blocksMetadata = new Gson().fromJson(new BufferedReader(new FileReader(metadataPath)), BlocksMetaData.class);
		BlockInfos binfo = blocksMetadata.getBlocksInfo().get(id);
		
//		final ExecutorService service = Threads.createExService(1);
//		Block block = new Block(service, binfo);
		
//		JDataFile inputData = new JDataFile.Builder().file(JFile.of(input)).load().getDataInfos().build();

//		JDataFile outputData = new JDataFile.Builder().file(JFile.of(output)).load().getDataInfos().build();

	
//		RandomAccessibleInterval<FloatType> tmp =  BlocksManager.getBlock(inputData.getLoader().fuse(), block, new Callback());

		RandomAccessibleInterval<FloatType>  result = Fusion.Fusion(input, binfo.getOffset(),binfo.getBlockSize());
//		ImageJFunctions.show(result,id+" result");
		
//		RandomAccessibleInterval<FloatType>  result = task.start(inputData.getLoader().fuse(), blockId , new Callback());
//		RandomAccessibleInterval<FloatType>  result = task.start(this.input, params , new Callback());
		
//		RandomAccessibleInterval<FloatType> outImage = outputData.getLoader().fuse();
		
//		block.pasteBlock(outImage , tmp, new Callback());
		System.out.println(id+"-save block size = "+Util.printCoordinates(Tools.dimensions(result))+"position: "+Util.printCoordinates(binfo.getOffset()));
		
		N5IO.saveBlock(output, result, binfo.getOffset());
		System.out.println(id+ "- Finish!");

		
	}


	public static void prestart(String metadataPath, String output) throws IOException {
		System.out.println("Start create output ");
		BlocksMetaData blocksMetadata = new Gson().fromJson(new BufferedReader(new FileReader(metadataPath)), BlocksMetaData.class);
		int[] blocks = Arrays.stream(blocksMetadata.getBlocksize()).mapToInt(i -> (int)i).toArray();
		N5IO.createBackResult(output, blocksMetadata.getDimensions(), blocks);
		System.out.println("Output created: "+output);
		System.out.println("Finish create output !");
	}

}
