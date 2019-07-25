package net.preibisch.distribution.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class Tools {

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

	public static long[] dimensions(RandomAccessibleInterval<FloatType> block) {
		long[] dims = new long[block.numDimensions()];
		for (int i = 0; i < block.numDimensions(); i++) {
			dims[i] = block.dimension(i);
		}
		return dims;
	}

	public static long[] sum(long[] offset, long[] blocksize) {
		long[] result = new long[offset.length];
		for (int i = 0; i < offset.length; i++) {
			result[i] = offset[i] + blocksize[i];
		}
		return result;
	}

//	public static void cleanFolder(String dir) throws IOException {
//		File folder = new File(dir);
//		FileUtils.deleteDirectory(folder);
//		folder.mkdir();
//	}

	public static void deleteRecursively(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteRecursively(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	public static String id() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
