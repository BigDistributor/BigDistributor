package net.preibisch.distribution.headless;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.tools.Tools;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class GenerateMetaBlocksFile implements Callable<Void> {

	@Option(names = { "-d", "--data" }, required = true, description = "The path of the Data")
	private String dataPath;

	@Option(names = { "-s", "--blockSize" }, required = true, description = "The size of the expected blocks en pixels")
	private Integer blockSize;
	
	@Option(names = { "-o", "--overlap" }, required = true, description = "The size of overlap between blocks")
	private int overlap;

	@Option(names = { "-path" }, required = true, description = "The path of the output file")
	private String outpath;

	public static void main(String[] args) {
		CommandLine.call(new GenerateMetaBlocksFile(), args);
		System.exit(0);
	}

	@Override
	public Void call() throws IncompatibleTypeException, SpimDataException, IOException {
		AbstractCallBack callback = createCallBack();
		callback.log("Create metablocks.. ");
		XMLFile inputData = XMLFile.XMLFile(dataPath);
		int[] blocksizes = Tools.array(BlockConfig.BLOCK_UNIT, inputData.getDims().length);
		Map<Integer, BasicBlockInfo> blocks = MetadataGenerator.generateBlocks(inputData.bb(), blocksizes);
		BlocksMetaData md = new BlocksMetaData(blocks, Util.int2long(blocksizes), inputData.bb().getDimensions(1),blocks.size());
		md.toJson(new File(outpath));
		callback.log("Success: Metablocks created !");
		callback.log("Metablocks path: "+outpath);
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
