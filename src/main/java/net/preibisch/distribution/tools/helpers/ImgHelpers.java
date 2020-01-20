package net.preibisch.distribution.tools.helpers;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class ImgHelpers {
	
	public static <T> long[] getDimensions(RandomAccessibleInterval<T> image) {
		long[] dimensions = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			dimensions[d] = image.dimension(d);
			System.out.println(d + " - " + image.dimension(d));
		}
		return dimensions;
	}
}
