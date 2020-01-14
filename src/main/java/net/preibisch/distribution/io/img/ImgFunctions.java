package net.preibisch.distribution.io.img;

import java.io.IOException;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;

public interface ImgFunctions<T> {
	public  RandomAccessibleInterval<T> fuse() throws IOException;
	public  RandomAccessibleInterval<T> fuse(int i) throws IOException;

	public  RandomAccessibleInterval<T> fuse(Interval bb,int i) throws IOException;
	
	public long[] getDimensions(int downsampling);
}
