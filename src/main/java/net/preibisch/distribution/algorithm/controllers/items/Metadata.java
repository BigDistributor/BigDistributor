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
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfo;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class Metadata {
	private String jobID;
	private String input;
	private String output;
	private BoundingBox bb;
	private long[] blocksize;
	private Map<Integer, BasicBlockInfo> blocksInfo;

	
	public Metadata(String jobID, String input, String output,BoundingBox bb, long[] blocksize,
			Map<Integer, BasicBlockInfo> blocksInfo) {
		super();
		this.bb = bb;
		this.jobID = jobID;
		this.input = input;
		this.output = output;
		this.blocksize = blocksize;
		this.blocksInfo = blocksInfo;
	}

	public BoundingBox getBb() {
		return bb;
	}
	
	public String getJobID() {
		return jobID;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public long[] getBlocksize() {
		return blocksize;
	}

	public Map<Integer, BasicBlockInfo> getBlocksInfo() {
		return blocksInfo;
	}

	public int size() {
		return blocksInfo.size();
	}

	@Override
	public String toString() {
		String str = "\nMetaData: total:" + blocksInfo.size() + " blocks"+ Util.printCoordinates(blocksize) + "\n" ;
//		+ " XMLFile: " + xml.toString()+"\n"
		String elms = "";
		for (int i = 0; i < blocksInfo.size(); i++) {
			elms = elms + i + "-" + blocksInfo.get(i).toString() + " \n";
		}
		return str + elms;
	}

	public void toJson(File file) throws IOException {
		Writer writer = new FileWriter(file);
		Gson gson = new GsonBuilder().create();
		gson.toJson(this, writer);
		System.out.println("Metadata saved: "+file.getAbsolutePath());
	}
	
	public static Metadata fromJson(String path) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		return new Gson().fromJson(new FileReader(path), Metadata.class);
	}
}