package net.preibisch.bigdistributor.tools.helpers;

public class LogHelpers {
	
	public static String logArray(long[] dims) {
		String log = "";
		for (long elm : dims)
			log += elm + ",";
		return log;
	}
}
