package net.preibisch.distribution.headless;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.models.AffineModel1D;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.task.params.FusionParams;
import net.preibisch.distribution.tools.helpers.IOHelpers;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.fusion.transformed.nonrigid.NonRigidParameters;
import net.preibisch.mvrecon.process.interestpointregistration.pairwise.constellation.grouping.Group;

public class Clustering {

//	public static void run(TaskType task, String xml, double downsampling, BoundingBox interval,
//			List<Group<ViewDescription>> groups) {
//		
//		String taskFile = TaskType.getTaskFile(task);
//		FusionParams params = new FusionParams(new File(xml).getName(), groups, interval, downsampling);
//		try {
//			ClusterWorkflow.run(params, xml,taskFile);
//		} catch (IOException | JSchException | SftpException | SpimDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void fuse(SpimData2 spimData,Map< ViewId, AffineTransform3D > registrations, Set<ViewDescription> views, boolean useBlending,
			boolean useContentBased, int interpolation, Interval bb, double downsampling, HashMap< ViewId, AffineModel1D > intensityAdjustment) {
		String xml = IOHelpers.getXML(spimData.getBasePath().getAbsolutePath());
		String taskFile = TaskType.getTaskFile(TaskType.FUSION);


		FusionParams params = new FusionParams(xml, bb, downsampling, registrations, views, useBlending, useContentBased, interpolation, intensityAdjustment);
		File f = new File("/Users/Marwan/Desktop/Task/test.json");
		params.toJson(f);
		try {
			Desktop.getDesktop().open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FusionParams p2 = FusionParams.fromJson(f);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void fuseNonRigid(SpimData2 spimData, HashMap<ViewId, AffineTransform3D> registrations,
			Set<ViewDescription> views, ArrayList<ViewId> viewsToUse, NonRigidParameters nonRigidParameters,
			boolean useBlending, boolean useContentBased, boolean virtualGrid, int interpolation, Interval boundingBox,
			double downsampling, HashMap< ViewId, AffineModel1D > intensityAdjustment) {
		String xml = IOHelpers.getXML(spimData.getBasePath().getAbsolutePath());
		String taskFile = TaskType.getTaskFile(TaskType.NON_RIGID);
		// TODO Auto-generated method stub
		
	}
}
