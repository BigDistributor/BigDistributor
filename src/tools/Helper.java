package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ObjectArrays;

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

	public static void log(String string) {
		System.out.println(string);
		Config.log.add(string);
	}

	public static List<String[]> generateBlocksPerJob(String[] localBlocksfiles, int jobs) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		int part = localBlocksfiles.length / jobs;
		int rest = localBlocksfiles.length % jobs;
		for (int i = 0; i < jobs; i++) {
			String[] temp = Arrays.copyOfRange(localBlocksfiles, i * part, (i + 1) * part);
			log("Job " + (i + 1) + ":[" + (i * part) + "-" + ((i + 1) * part - 1) + "|" + temp.length+"/"+localBlocksfiles.length + "]:"
					+ String.join("|", temp));
			if (i < jobs - 1) {
				list.add(temp);
			} else {
				String[] restArray = Arrays.copyOfRange(localBlocksfiles, jobs - 1, jobs - 1 + rest);
				log("with Rest: "+ String.join("|", restArray));
				list.add(ObjectArrays.concat(temp, restArray, String.class));
			}
		}
		return list;
	}

}
