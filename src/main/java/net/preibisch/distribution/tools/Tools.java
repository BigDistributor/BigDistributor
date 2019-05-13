package main.java.net.preibisch.distribution.tools;

import java.util.Arrays;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class Tools {

	public static long[] fill(long val, int length) {
		long[] arr  = new long[length];
		Arrays.fill(arr , val);
		return arr;
	}

	public static long[] dimensions(RandomAccessibleInterval<FloatType> block) {
		long[] dims = new long[block.numDimensions()];
		for(int i = 0; i < block.numDimensions();i++) {
			dims[i]= block.dimension(i);
		}
		return dims;
	}
	
	public static long[] sum(long[] offset, long[] blocksize) {
		long[] result = new long[offset.length];
		for(int i=0; i<offset.length;i++) {
			result[i]=offset[i]+blocksize[i];
		}
		return result;
	}

}
