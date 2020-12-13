package net.preibisch.bigdistributor.algorithm.errorhandler.logmanager;

import net.preibisch.bigdistributor.gui.items.DataPreview;
import net.preibisch.bigdistributor.gui.items.basics.Colors;

public class ClusterLogProcessor {

	public static void process(String log) {
		try {
			String[] parts = log.split(";");

			int id = Integer.parseInt(parts[2]);
			DataPreview.getBlocksPreview().get(id).setStatus(Colors.PROCESSED);
		} catch (Exception e) {
			MyLogger.log().error(e);
		}

	}

}
