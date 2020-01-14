package net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.task.SerializingFunctions;

public class BlocksMetaData extends SerializingFunctions {
	private int total;
	private long[] blocksize;
	private int blockUnit;
	private String jobId;
	private Map<Integer, BasicBlockInfo> blocksInfo;

	public BlocksMetaData(Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes, long[] dimensions, int total) {
		this(Job.getId(),  blocksInfo, bsizes, BlockConfig.BLOCK_UNIT, total);
	}

	public BlocksMetaData( Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes, int blockUnit,
			long[] dimensions, int total) {
		this(Job.getId(),  blocksInfo, bsizes, blockUnit, total);
	}

	public BlocksMetaData(String jobId, Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes,
			int blockUnit, int total) {
		super();
		this.jobId = jobId;
		this.blockUnit = blockUnit;
		this.total = total;
		this.blocksize = bsizes;
		this.blocksInfo = blocksInfo;
	}

	public String getJobId() {
		return jobId;
	}

	public Map<Integer, BasicBlockInfo> getBlocksInfo() {
		return blocksInfo;
	}

	public int getBlockUnit() {
		return blockUnit;
	}

	public void setBlocksInfo(Map<Integer, BasicBlockInfo> blocksInfo) {
		this.blocksInfo = blocksInfo;
	}

	public long[] getBlocksize() {
		return blocksize;
	}

	public int getTotal() {
		return total;
	}

	public void setBlocksize(long[] blocksize) {
		this.blocksize = blocksize;
	}

	@Override
	public String toString() {
		String str = "\nMetaData: total:" + blocksInfo.size()
				+ " blocks: " + Util.printCoordinates(blocksize) + "\n" +
				" blockUnit: " + blockUnit + "\n";
		String elms = "";
		for (int i = 0; i < blocksInfo.size() % 10; i++) {
			elms = elms + i + "-" + blocksInfo.get(i).toString() + " \n";
		}
		// String elm1 = blocksInfo.get(1).toString()+" \n";
		return str + elms;
	}

	public void toJson(File file) throws IOException {
		Writer writer = new FileWriter(file);
		Gson gson = new GsonBuilder().create();
		gson.toJson(this, writer);
		System.out.println("Metadata saved: " + file.getAbsolutePath());
	}

	public static BlocksMetaData fromJson(String path)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		return new Gson().fromJson(new FileReader(path), BlocksMetaData.class);
	}
}
