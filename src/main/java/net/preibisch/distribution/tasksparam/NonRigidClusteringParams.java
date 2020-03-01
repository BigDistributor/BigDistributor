package net.preibisch.distribution.tasksparam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mpicbg.models.AffineModel1D;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.task.params.ParamJsonHelpers;
import net.preibisch.distribution.algorithm.task.params.ParamsJsonSerialzer;
import net.preibisch.distribution.tools.helpers.ArrayHelpers;
import net.preibisch.mvrecon.Threads;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.fusion.transformed.nonrigid.NonRigidParameters;
import net.preibisch.mvrecon.process.fusion.transformed.nonrigid.NonRigidTools;

public class NonRigidClusteringParams extends ParamJsonHelpers implements ParamsJsonSerialzer<NonRigidClusteringParams> {
	private Map<ViewId, AffineTransform3D> viewRegistrations;
	private Collection<? extends ViewId> viewsToFuse;
	private Collection<? extends ViewId> viewsToUse;
	private boolean useBlending;
	private boolean useContentBased;
	private NonRigidParameters nonRigidParameters;
	private boolean virtualGrid;
	private int interpolation;
	private Interval boundingBox;
	private double downsampling;
	private Map<? extends ViewId, AffineModel1D> intensityAdjustments;

	
	public NonRigidClusteringParams() {}
	public NonRigidClusteringParams(Map<ViewId, AffineTransform3D> viewRegistrations,
			Collection<? extends ViewId> viewsToFuse, Collection<? extends ViewId> viewsToUse, boolean useBlending,
			boolean useContentBased, NonRigidParameters nonRigidParameters, boolean virtualGrid, int interpolation,
			Interval boundingBox, double downsampling, Map<? extends ViewId, AffineModel1D> intensityAdjustments) {
		super();
		this.viewRegistrations = viewRegistrations;
		this.viewsToFuse = viewsToFuse;
		this.viewsToUse = viewsToUse;
		this.useBlending = useBlending;
		this.useContentBased = useContentBased;
		this.nonRigidParameters = nonRigidParameters;
		this.virtualGrid = virtualGrid;
		this.interpolation = interpolation;
		this.boundingBox = boundingBox;
		this.downsampling = downsampling;
		this.intensityAdjustments = intensityAdjustments;
	}

	public Map<ViewId, AffineTransform3D> getViewRegistrations() {
		return viewRegistrations;
	}

	public Collection<? extends ViewId> getViewsToFuse() {
		return viewsToFuse;
	}

	public Collection<? extends ViewId> getViewsToUse() {
		return viewsToUse;
	}

	public boolean useBlending() {
		return useBlending;
	}

	public boolean useContentBased() {
		return useContentBased;
	}

	public boolean virtualGrid() {
		return virtualGrid;
	}

	public int getInterpolation() {
		return interpolation;
	}

	public Interval getBoundingBox() {
		return boundingBox;
	}

	public double getDownsampling() {
		return downsampling;
	}

	public Map<? extends ViewId, AffineModel1D> getIntensityAdjustments() {
		return intensityAdjustments;
	}	
	
	public NonRigidParameters getNonRigidParameters() {
		return nonRigidParameters;
	}

	public RandomAccessibleInterval<FloatType> process(String input,BoundingBox bb) throws SpimDataException{
		final ExecutorService taskExecutor = Executors.newFixedThreadPool( Threads.numThreads() );
		SpimData2 spimdata = getSpimData(input);
		return NonRigidTools.fuseVirtualInterpolatedNonRigid(
				spimdata.getSequenceDescription().getImgLoader(),
				getViewRegistrations(),
				spimdata.getViewInterestPoints().getViewInterestPoints(),
				spimdata.getSequenceDescription().getViewDescriptions(),
				getViewsToFuse(),
				getViewsToUse(),
				getNonRigidParameters().getLabels(),
				useBlending(),
				useContentBased(),
				getNonRigidParameters().showDistanceMap(),
				ArrayHelpers.fill( getNonRigidParameters().getControlPointDistance(), 3 ),
				getNonRigidParameters().getAlpha(),
				false,
				getInterpolation(),
//				params.getBoundingBox(),
				bb,
				getDownsampling(),
				getIntensityAdjustments(),
				taskExecutor ).getA();
	}
	@Override
	public NonRigidClusteringParams fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		return NonRigidClusteringParams.getGson().fromJson(new FileReader(file), NonRigidClusteringParams.class);
	}

	@Override
	public void toJson(File file) {
		try (PrintWriter out = new PrintWriter(file)) {
			String json = getGson().toJson(this);
			out.print(json);
			out.flush();
			out.close();
			System.out.println("File saved: " + file.getAbsolutePath() + " | Size: " + file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
