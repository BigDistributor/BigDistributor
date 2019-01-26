package main.java.net.preibisch.distribution.headless;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JDataFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.imglib2.exception.IncompatibleTypeException;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class GenerateMetaBlocksFile implements Callable<Void> {

	@Option(names = { "-d", "--data" }, required = true, description = "The path of the Data")
	private String dataPath;

	@Option(names = { "-s", "--blockSize" }, required = true, description = "The size of the expected blocks en pixels")
	private Integer blockSize;
	
	@Option(names = { "-o", "--overlap" }, required = true, description = "The size of overlap between blocks")
	private Integer overlap;

	@Option(names = { "-path" }, required = true, description = "The path of the output file")
	private String outpath;

	public static void main(String[] args) {
		CommandLine.call(new GenerateMetaBlocksFile(), args);
		System.exit(0);
	}

	@Override
	public Void call() throws FileNotFoundException, IncompatibleTypeException {
		AbstractCallBack callback = createCallBack();
		callback.log("Create metablocks.. ");
		JDataFile inputData = new JDataFile.Builder().file(JFile.of(dataPath)).load().getDataInfos().build();
//		RandomAccessibleInterval<FloatType> image = inputData.getLoader().fuse();
		
		
		long[] dims = inputData.getDimensions();
		final long[] bsizes  = new long[dims.length];
		Arrays.fill(bsizes , blockSize);
		Map<Integer, BlockInfos> metainfo = MetaDataGenerator.generateBlocks(bsizes, dims,overlap, callback);
		BlocksMetaData md = new BlocksMetaData(metainfo);
		String metaPath = MetaDataGenerator.createJSon(md,outpath, callback);
		callback.log("Success: Metablocks created !");
		callback.log("Metablocks path: "+metaPath);
		return null;
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
