package net.preibisch.bigdistributor.tools.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.preibisch.bigdistributor.algorithm.controllers.items.Job;

public class IDManager {

	private Job.ProcessMode mode;
	private String id;

	private static final Map<Job.ProcessMode, String> nonAcceptableKeys;
	static {
		nonAcceptableKeys = new HashMap<>();
		nonAcceptableKeys.put(Job.ProcessMode.AWS_PROCESSING, "_");
		nonAcceptableKeys.put(Job.ProcessMode.LOCAL_PROCESSING, "");
		nonAcceptableKeys.put(Job.ProcessMode.CLUSTER_PROCESSING, "");
	}

	public IDManager(Job.ProcessMode mode) {
		this.mode = mode;
		this.id = format(id());
	}

	private String format(String id) {
		String keys = nonAcceptableKeys.get(mode);
		String result = id.toLowerCase();
		for (char ch : keys.toCharArray())
			result.replace(Character.toString(ch), "");
		return result;
	}

	private static String id() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String id = UUID.randomUUID().toString().replace("-", "");
		return timeStamp + "-" + id;
	}

	public String get() {
		return id;
	}

	// public static List<String[]> generateBlocksPerJob(String[] localBlocksfiles,
	// int jobs, AbstractCallBack callBack) {
	// ArrayList<String[]> list = new ArrayList<String[]>();
	// int part = localBlocksfiles.length / jobs;
	// int rest = localBlocksfiles.length % jobs;
	// int i=0;
	// if (part > 0) {
	// for (i = 0; i < jobs - 1; i++) {
	// String[] temp = Arrays.copyOfRange(localBlocksfiles, i * part, (i + 1) *
	// part);
	// callBack.log("Job " + (i + 1) + ":[" + (i * part) + "-" + ((i + 1) * part -
	// 1) + "|" + temp.length + "/"
	// + localBlocksfiles.length + "]:" + String.join("|", temp));
	// list.add(temp);
	// }
	// }
	// String[] restArray = Arrays.copyOfRange(localBlocksfiles, i * part, i * part
	// + rest);
	// callBack.log("with Rest: " + String.join("|", restArray));
	// list.add(restArray);
	//
	// // list.add(ObjectArrays.concat(temp, restArray, String.class));
	// return list;
	//
	// }
}
