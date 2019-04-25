package main.java.net.preibisch.distribution.plugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.google.gson.Gson;

import main.java.net.preibisch.distribution.algorithm.AbstractTask2;
import main.java.net.preibisch.distribution.algorithm.blockmanager.Block;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlocksManager;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JDataFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.multithreading.Threads;
import main.java.net.preibisch.distribution.headless.BlockExtractorUsingMetaBlocks;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.plugin.resave.Resave_HDF5;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MainJob<T, R, K, V> implements Callable<Void> {

	@Option(names = { "-out", "--output" }, required = true, description = "The path of the Data")
	private String output;
	
	@Option(names = { "-in", "--input" }, required = true, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = true, description = "The path of the MetaData file")
	private String metadataPath;

	@Option(names = { "-id" }, required = true, description = "The id of block")
	private Integer id;

	private AbstractTask2<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<FloatType>, String, Integer> task;
	
	
	public MainJob( AbstractTask2<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<FloatType>, String, Integer> task) {
		this.task = task;
	}


	


	@Override
	public Void call() throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(metadataPath));
		BlocksMetaData blocksMetadata = new Gson().fromJson(br, BlocksMetaData.class);
		BlockInfos binfo = blocksMetadata.get().get(id);
		final ExecutorService service = Threads.createExService(1);
		Block block = new Block(service, binfo);
		
		JDataFile inputData = new JDataFile.Builder().file(JFile.of(input)).load().getDataInfos().build();

		JDataFile outputData = new JDataFile.Builder().file(JFile.of(output)).load().getDataInfos().build();

	
		RandomAccessibleInterval<FloatType> tmp =  BlocksManager.getBlock(inputData.getLoader().fuse(), block, new Callback());
		
		Map<String, Integer> params = new HashMap<>();
		params.put("sigma", 10);
		task.start(inputData.getLoader().fuse(), outputData.getLoader().fuse(), params , new Callback());
		
		block.pasteBlock(outputData.getLoader().fuse(), tmp, new Callback());
		return null;
	}

}
