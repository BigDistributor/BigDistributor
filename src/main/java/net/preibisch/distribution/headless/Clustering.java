package net.preibisch.distribution.headless;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import net.imglib2.Interval;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.task.FusionParams;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.interestpointregistration.pairwise.constellation.grouping.Group;

public class Clustering {

	public static void run(TaskType task, String xml, double downsampling, BoundingBox interval,
			List<Group<ViewDescription>> groups) {
		
		String taskFile = TaskType.getTaskFile(task);
		FusionParams params = new FusionParams(new File(xml).getName(), groups, interval, downsampling);
		try {
			ClusterWorkflow.run(params, xml,taskFile);
		} catch (IOException | JSchException | SftpException | SpimDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
