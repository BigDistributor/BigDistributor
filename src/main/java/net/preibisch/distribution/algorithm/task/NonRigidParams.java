package net.preibisch.distribution.algorithm.task;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mpicbg.models.AffineModel1D;
import mpicbg.spim.data.generic.sequence.BasicImgLoader;
import mpicbg.spim.data.generic.sequence.BasicViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.mvrecon.fiji.spimdata.interestpoints.ViewInterestPointLists;

public class NonRigidParams {
	private BasicImgLoader imgloader;
	private Map<ViewId, AffineTransform3D> viewRegistrations;
	private Map<ViewId, ViewInterestPointLists> viewInterestPoints;
	private Map<ViewId, ? extends BasicViewDescription<?>> viewDescriptions;
	private Collection<? extends ViewId> viewsToFuse;
	private Collection<? extends ViewId> viewsToUse;
	private ArrayList<String> labels;
	private boolean useBlending;
	private boolean useContentBased;
	private boolean displayDistances;
	private long[] controlPointDistance;
	private double alpha;
	private boolean virtualGrid;
	private int interpolation;
	private Interval boundingBox;
	private double downsampling;
	private Map<? extends ViewId, AffineModel1D> intensityAdjustments;

	
	public NonRigidParams(BasicImgLoader imgloader, Map<ViewId, AffineTransform3D> viewRegistrations,
			Map<ViewId, ViewInterestPointLists> viewInterestPoints,
			Map<ViewId, ? extends BasicViewDescription<?>> viewDescriptions, Collection<? extends ViewId> viewsToFuse,
			Collection<? extends ViewId> viewsToUse, ArrayList<String> labels, boolean useBlending,
			boolean useContentBased, boolean displayDistances, long[] controlPointDistance, double alpha,
			boolean virtualGrid, int interpolation, Interval boundingBox, double downsampling,
			Map<? extends ViewId, AffineModel1D> intensityAdjustments) {
		super();
		this.imgloader = imgloader;
		this.viewRegistrations = viewRegistrations;
		this.viewInterestPoints = viewInterestPoints;
		this.viewDescriptions = viewDescriptions;
		this.viewsToFuse = viewsToFuse;
		this.viewsToUse = viewsToUse;
		this.labels = labels;
		this.useBlending = useBlending;
		this.useContentBased = useContentBased;
		this.displayDistances = displayDistances;
		this.controlPointDistance = controlPointDistance;
		this.alpha = alpha;
		this.virtualGrid = virtualGrid;
		this.interpolation = interpolation;
		this.boundingBox = boundingBox;
		this.downsampling = downsampling;
		this.intensityAdjustments = intensityAdjustments;
	}

	public void toJson(String path) throws JsonIOException, IOException {
		Gson gson = new Gson();

		gson.toJson(this, new FileWriter(path));

	}

	public NonRigidParams fromJson(String path) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		NonRigidParams params = gson.fromJson(new FileReader(path), NonRigidParams.class);
		return params;
	}
}
