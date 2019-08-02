package net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;

public class BlocksMetaData {
	private int total;
	private long[] dimensions;
	private long[] blocksize;
	private int blockUnit;
	private int downsample;
	private String jobId;
	private Map<Integer, BasicBlockInfo> blocksInfo;
	private List<ViewIdMD> viewIdsmd;

	public BlocksMetaData(Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes, long[] dimensions, int total) {
		this(Job.getId(), null, blocksInfo, bsizes, BlockConfig.BLOCK_UNIT, dimensions, total, 1);
	}

	public BlocksMetaData(List<ViewId> viewIds, Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes, int blockUnit,
			long[] dimensions, int total, int down) {
		this(Job.getId(), viewIds, blocksInfo, bsizes, blockUnit, dimensions, total, down);
	}

	public BlocksMetaData(String jobId, List<ViewId> viewIds, Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes,
			int blockUnit, long[] dimensions, int total, int down) {
		super();
		this.jobId = jobId;
		this.downsample = down;
		this.viewIdsmd = setViewIds(viewIds);
		this.blockUnit = blockUnit;
		this.total = total;
		this.dimensions = dimensions;
		this.blocksize = bsizes;
		this.blocksInfo = blocksInfo;
	}

	private List<ViewIdMD> setViewIds(List<ViewId> viewIds) {
		List<ViewIdMD> viewIdMD = new ArrayList<>();
		for (ViewId viewid : viewIds) {
			viewIdMD.add(new ViewIdMD(viewid));
		}
		return viewIdMD;
	}

	public String getJobId() {
		return jobId;
	}

	public Map<Integer, BasicBlockInfo> getBlocksInfo() {
		return blocksInfo;
	}

	public List<ViewId> getViewIds() {
		List<ViewId> viewIds = new ArrayList<>();
		for (ViewIdMD viewid : viewIdsmd) {
			viewIds.add(new ViewId(viewid.getTimepoint(), viewid.getSetup()));
		}
		return viewIds;
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

	public long[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(long[] dimensions) {
		this.dimensions = dimensions;
	}

	public int getDownsample() {
		return downsample;
	}

	@Override
	public String toString() {
		String str = "\nMetaData: total:" + blocksInfo.size() + " dims:" + Util.printCoordinates(dimensions)
				+ " blocks: " + Util.printCoordinates(blocksize) + "\n" + " downsample: " + downsample + "\n"
				+ " viewIds: " + viewIdsmd + "\n" + " blockUnit: " + blockUnit + "\n";
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
