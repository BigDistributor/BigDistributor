package net.preibisch.distribution.tools.helpers;

import java.util.Arrays;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class ArrayHelpers {
	public static int[] array(int unit, int size) {
		int[] array = new int[size];
		Arrays.fill(array, unit);
		return array;
	}
	
	public static long[] fill(long val, int length) {
		long[] arr = new long[length];
		Arrays.fill(arr, val);
		return arr;
	}

	public static long[] sum(long[] offset, long[] blocksize) {
		long[] result = new long[offset.length];
		for (int i = 0; i < offset.length; i++) {
			result[i] = offset[i] + blocksize[i];
		}
		return result;
	}

	public static long[] dims(RandomAccessibleInterval<FloatType> image) {
		long[] dims = new long[image.numDimensions()];
		for (int i = 0; i < dims.length; i++) {
			dims[i] = image.dimension(i);
		}
		return dims;
	}
}
