package net.preibisch.distribution.algorithm.task.params;

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
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.generic.sequence.BasicImgLoader;
import mpicbg.spim.data.generic.sequence.BasicViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.interestpoints.ViewInterestPointLists;
import net.preibisch.mvrecon.process.fusion.transformed.nonrigid.NonRigidParameters;

public class NonRigidParams {
	private String xml;
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

	
	public SpimData2 getSpimData() throws SpimDataException {
		return new XmlIoSpimData2( "" ).load(xml);
	}

	public NonRigidParams(String xml, Map<ViewId, AffineTransform3D> viewRegistrations,
			Collection<? extends ViewId> viewsToFuse, Collection<? extends ViewId> viewsToUse, boolean useBlending,
			boolean useContentBased, NonRigidParameters nonRigidParameters, boolean virtualGrid, int interpolation,
			Interval boundingBox, double downsampling, Map<? extends ViewId, AffineModel1D> intensityAdjustments) {
		super();
		this.xml = xml;
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



	public void toJson(String path) throws JsonIOException, IOException {
		Gson gson = new Gson();

		gson.toJson(this, new FileWriter(path));

	}

	public static NonRigidParams fromJson(String path) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		NonRigidParams params = gson.fromJson(new FileReader(path), NonRigidParams.class);
		return params;
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
	
}
