package main.java.net.preibisch.distribution.headless;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.google.gson.Gson;

import main.java.net.preibisch.distribution.algorithm.blockmanager.Block;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlocksManager;
import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JDataFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.algorithm.multithreading.Threads;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generator", description = "To give the task just one block from the data, "
		+ "This generator take as parameter the data, the metadata and the id of the block needed "
		+ "and the generate a block as tif file", version = "generator 0.1")

public class BlockExtractorUsingMetaBlocks implements Callable<Void> {

	@Option(names = { "-d", "--data" }, required = true, description = "The path of the Data")
	private String dataPath;

	@Option(names = { "-m", "--meta" }, required = true, description = "The path of the MetaData file")
	private String metadataPath;

	@Option(names = { "-id" }, required = true, description = "The path of the MetaData file")
	private Integer id;

	@Option(names = { "-path" }, required = true, description = "The path of the output file")
	private String path;

	public static void main(String[] args) {
		CommandLine.call(new BlockExtractorUsingMetaBlocks(), args);
		System.exit(0);
	}

	@Override
	public Void call() throws FileNotFoundException, IncompatibleTypeException {
		BufferedReader br;

		br = new BufferedReader(new FileReader(metadataPath));
		BlocksMetaData blocksMetadata = new Gson().fromJson(br, BlocksMetaData.class);
		BlockInfos binfo = blocksMetadata.getBlocksInfo().get(id);
		final ExecutorService service = Threads.createExService(1);
		Block block = new Block(service, binfo);
		JDataFile inputData = new JDataFile.Builder().file(JFile.of(dataPath)).load().getDataInfos().build();
		RandomAccessibleInterval<FloatType> image = inputData.getLoader().fuse();
		AbstractCallBack callBack = createCallBack();
		BlocksManager.saveBlock(image, binfo.getBlockSize(), path, id, block, callBack);

		return null;
	}
	
	public static RandomAccessibleInterval<FloatType> getBlock(String dataPath,Block block, AbstractCallBack callback) throws FileNotFoundException, IncompatibleTypeException{

		JDataFile inputData = new JDataFile.Builder().file(JFile.of(dataPath)).load().getDataInfos().build();
		RandomAccessibleInterval<FloatType> image = inputData.getLoader().fuse();
	
		return BlocksManager.getBlock(image, block, callback);
	}
	
	
	
	
	public static void setBlock(RandomAccessibleInterval<FloatType> resultImage, RandomAccessibleInterval<FloatType> tmp, Block block, AbstractCallBack callback) {

			block.pasteBlock(resultImage, tmp, callback);
	
	}

	private static AbstractCallBack createCallBack() {
		AbstractCallBack callback = new AbstractCallBack() {

			@Override
			public void onSuccess(int pos) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(String error) {
				System.out.println("ERROR: "+error);

			}

			@Override
			public void log(String log) {
				System.out.println(log);

			}
		};
		// TODO Auto-generated method stub
		return callback;
	}

}
