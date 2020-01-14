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
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.io.img.XMLFile;

public class BlocksMetaData2 {
	private int total;
	private XMLFile xml;
	private long[] blocksize;
	private Map<Integer, BasicBlockInfo> blocksInfo;

	public BlocksMetaData2(Map<Integer, BasicBlockInfo> blocksInfo, long[] bsizes, XMLFile xml, int total) {
		super();
		this.total = total;
		this.xml = xml;
		this.blocksize = bsizes;
		this.blocksInfo = blocksInfo;
	}

	public Map<Integer, BasicBlockInfo> getBlocksInfo() {
		return blocksInfo;
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

	public XMLFile getXML() {
		return xml;
	}

	public void setDimensions(XMLFile xml) {
		this.xml = xml;
	}

	@Override
	public String toString() {
		String str = "\nMetaData: total:" + blocksInfo.size() + " blocks"+ Util.printCoordinates(blocksize) + "\n"+ " XMLFile: " + xml.toString()+"\n" ;
		String elms = "";
		for (int i = 0; i < blocksInfo.size(); i++) {
			elms = elms + i + "-" + blocksInfo.get(i).toString() + " \n";
		}
//		String elm1 = blocksInfo.get(1).toString()+" \n";
		return str + elms;
	}

	public void toJson(File file) throws IOException {
		Writer writer = new FileWriter(file);
		Gson gson = new GsonBuilder().create();
		gson.toJson(this, writer);
		System.out.println("Metadata saved: "+file.getAbsolutePath());
	}
	
	public static BlocksMetaData fromJson(String path) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		return new Gson().fromJson(new FileReader(path), BlocksMetaData.class);
	}
}

