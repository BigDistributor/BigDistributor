package net.preibisch.distribution.headless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.google.gson.Gson;

import mpicbg.spim.data.SpimDataException;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.blockmanagement.ImageBlocksManager;
import net.preibisch.distribution.algorithm.blockmanagement.block.Block;
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.ComplexBlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.multithreading.Threads;
import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.img.XMLFile;
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
	public Void call() throws IncompatibleTypeException, IOException, SpimDataException {
		BufferedReader br;

		br = new BufferedReader(new FileReader(metadataPath));
		BlocksMetaData blocksMetadata = new Gson().fromJson(br, BlocksMetaData.class);
		ComplexBlockInfo binfo = (ComplexBlockInfo) blocksMetadata.getBlocksInfo().get(id);
		final ExecutorService service = Threads.createExService(1);
		Block block = new Block(service, binfo);
		XMLFile inputData = XMLFile.XMLFile(dataPath);
		RandomAccessibleInterval<FloatType> image = inputData.getImg();
		ImageBlocksManager.saveOneBlock(image, binfo.getBlockSize(), new File(path),id, block,DataExtension.TIF);

		return null;
	}
	
	public static RandomAccessibleInterval<FloatType> getBlock(String dataPath,Block block) throws IncompatibleTypeException, SpimDataException, IOException{

		XMLFile inputData =  XMLFile.XMLFile(dataPath);
		RandomAccessibleInterval<FloatType> image = inputData.getImg();
	
		return ImageBlocksManager.getBlock(image, block);
	}
	
	public static void setBlock(RandomAccessibleInterval<FloatType> resultImage, RandomAccessibleInterval<FloatType> tmp, Block block) {
			block.pasteBlock(resultImage, tmp);
	}
}
