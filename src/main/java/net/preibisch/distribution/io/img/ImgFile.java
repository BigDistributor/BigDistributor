package net.preibisch.distribution.io.img;

import java.io.File;

import net.preibisch.distribution.algorithm.controllers.items.DataExtension;


public class ImgFile extends File {
	
	protected long[] dims;
	protected DataExtension extension;
	
	public ImgFile (String path) {
		super(path);
		this.extension = DataExtension.fromURI(path);
	}
	
	public long[] getDims() {
		return dims;
	}
	
	public DataExtension getExtension() {
		return extension;
	}

	public long[] getDimensions() {
		return dims;
	}

	
}
