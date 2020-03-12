package net.preibisch.distribution.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mpicbg.models.AffineModel1D;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.algorithm.task.params.ParamsJsonSerialzer;
import net.preibisch.distribution.algorithm.task.params.SerializableParams;
import net.preibisch.distribution.tasksparam.FusionClusteringParams;
import net.preibisch.distribution.tasksparam.NonRigidClusteringParams;
import net.preibisch.distribution.tools.helpers.IOHelpers;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.process.fusion.transformed.nonrigid.NonRigidParameters;

public class Clustering {
	private static List<SerializableParams> paramsForClusterTasks;
	private static String xml;
	private static TaskType type;

	public static void init(TaskType type) {
		Clustering.type = type;
		paramsForClusterTasks = new ArrayList<>();
	}

	public static void fuse(SpimData2 spimData, Map<ViewId, AffineTransform3D> registrations,
			Set<ViewDescription> views, boolean useBlending, boolean useContentBased, int interpolation, Interval bb,
			double downsampling, Map<ViewId, AffineModel1D> intensityAdjustment) {
		xml = IOHelpers.getXML(spimData.getBasePath().getAbsolutePath());
		FusionClusteringParams param = new FusionClusteringParams( bb, downsampling, registrations, views,
				useBlending, useContentBased, interpolation, intensityAdjustment);
		paramsForClusterTasks.add(param);
	}

	public static void fuseNonRigid(SpimData2 spimData, Map<ViewId, AffineTransform3D> registrations,
			Set<ViewDescription> views, List<ViewId> viewsToUse, NonRigidParameters nonRigidParameters,
			boolean useBlending, boolean useContentBased, boolean virtualGrid, int interpolation, Interval boundingBox,
			double downsampling, Map<ViewId, AffineModel1D> intensityAdjustment) {
		xml = IOHelpers.getXML(spimData.getBasePath().getAbsolutePath());
		NonRigidClusteringParams param = new NonRigidClusteringParams( registrations, views, viewsToUse,
				useBlending, useContentBased, nonRigidParameters, virtualGrid, interpolation, boundingBox, downsampling,
				intensityAdjustment);
		paramsForClusterTasks.add(param);
	}

	public static void run(Interval interval) {
		try {
			ClusterWorkflow.run(paramsForClusterTasks, xml, type, interval);
		} catch (IOException | JSchException | SftpException | SpimDataException e) {
			MyLogger.log().error(e);
		}
	}

	private Clustering() {
		throw new IllegalStateException("Utility class");
	}

}