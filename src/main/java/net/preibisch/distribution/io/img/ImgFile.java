package main.java.net.preibisch.distribution.io.img;

import java.io.File;
import java.io.IOException;

import main.java.net.preibisch.distribution.algorithm.controllers.items.DataExtension;
import main.java.net.preibisch.distribution.io.img.load.Loader;
import main.java.net.preibisch.distribution.tools.Helper;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class ImgFile extends File implements ImgFunctions<FloatType> {
	
	protected long[] dims;
	protected DataExtension extension;
//	private Loader loader;
	
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

	@Override
	public RandomAccessibleInterval<FloatType> fuse() throws IOException{
		throw new IOException();
	}

	@Override
	public RandomAccessibleInterval<FloatType> fuse(BoundingBox bb) throws IOException {
		throw new IOException();
	}
	
}