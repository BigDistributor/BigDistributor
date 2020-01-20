package net.preibisch.distribution.tools.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class JobHelpers {
	public static String id() {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String id = UUID.randomUUID().toString().replace("-", "");
		return timeStamp+"_"+id;
	}
	
	public static List<String[]> generateBlocksPerJob(String[] localBlocksfiles, int jobs, AbstractCallBack callBack) {
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
}
