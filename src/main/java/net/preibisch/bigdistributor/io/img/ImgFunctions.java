package net.preibisch.bigdistributor.io.img;

import java.io.IOException;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public interface ImgFunctions<T> {
	public  RandomAccessibleInterval<FloatType> getImg() throws IOException;
//	public  RandomAccessibleInterval<T> getImg(int i) throws IOException;

//	public  RandomAccessibleInterval<T> fuse(Interval bb,int i) throws IOException;
	
//	public long[] getDimensions(int downsampling);
}
