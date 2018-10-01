package main.java.com.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.com.clustering.MyCallBack;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class Helper {
	

	public static long[] getDimensions(Img<FloatType> image) {
		long[] dimensions = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			dimensions[d] = image.dimension(d);
			System.out.println(d + " - " + image.dimension(d));
		}
		return dimensions;
	}

	public static List<String[]> generateBlocksPerJob(String[] localBlocksfiles, int jobs, MyCallBack callBack) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		int part = localBlocksfiles.length / jobs;
		int rest = localBlocksfiles.length % jobs;
		int i=0;
		if (part > 0) {
			for (i = 0; i < jobs - 1; i++) {
				String[] temp = Arrays.copyOfRange(localBlocksfiles, i * part, (i + 1) * part);
				callBack.log("Job " + (i + 1) + ":[" + (i * part) + "-" + ((i + 1) * part - 1) + "|" + temp.length + "/"
						+ localBlocksfiles.length + "]:" + String.join("|", temp));
				list.add(temp);
			}
		}
		String[] restArray = Arrays.copyOfRange(localBlocksfiles, i * part, i * part + rest);
		callBack.log("with Rest: " + String.join("|", restArray));
		list.add(restArray);

		// list.add(ObjectArrays.concat(temp, restArray, String.class));
		return list;

	}

	public static String logArray(long[] dims) {
		String log = "";
		for (long elm : dims)
			log += elm + ",";
		return log;
	}
	
	public static ArrayList<String> getFiles(String path, String prefix) {
		File folder = new File(path);
		String[] files = folder.list();
		ArrayList<String> result = new ArrayList<>();
		for (String file: files) {
			if (file.endsWith(prefix)) result.add(file);
		}
		return result;
	}

}
