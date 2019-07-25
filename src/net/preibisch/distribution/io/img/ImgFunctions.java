package net.preibisch.distribution.io.img;

import java.io.IOException;

import net.imglib2.RandomAccessibleInterval;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public interface ImgFunctions<T> {
	public  RandomAccessibleInterval<T> fuse() throws IOException;

	public  RandomAccessibleInterval<T> fuse(BoundingBox bb) throws IOException;
}
