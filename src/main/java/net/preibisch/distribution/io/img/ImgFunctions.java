package main.java.net.preibisch.distribution.io.img;

import net.imglib2.RandomAccessibleInterval;

public interface ImgFunctions<T> {
	public  RandomAccessibleInterval<T> fuse();
}
