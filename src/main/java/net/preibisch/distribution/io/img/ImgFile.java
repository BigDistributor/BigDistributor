package net.preibisch.distribution.io.img;

import java.io.File;
import java.io.IOException;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.controllers.items.DataExtension;


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
	public RandomAccessibleInterval<FloatType> fuse(int i) throws IOException{
		throw new IOException();
	}

	@Override
	public RandomAccessibleInterval<FloatType> fuse(Interval bb,int i) throws IOException {
		throw new IOException();
	}

	@Override
	public RandomAccessibleInterval<FloatType> fuse() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long[] getDimensions(int downsampling) {
		return dims;
		
	}
	
}
