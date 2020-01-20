package net.preibisch.distribution.algorithm.task.params;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import bdv.img.remote.AffineTransform3DJsonSerializer;
import mpicbg.models.AffineModel1D;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.distribution.io.serializers.viewid.ViewIdJsonSerializer;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class FusionParams{
	private String xml;
	private double downsampling;
	private Map< ViewId, AffineTransform3D > registrations;
	private Set<ViewDescription> views;
	private boolean useBlending;
	private boolean useContentBased;
	private int interpolation;
	private BoundingBox boundingBox;
	private HashMap< ViewId, AffineModel1D > intensityAdjustment;

	public void toJson(File file)  {
		try (PrintWriter out = new PrintWriter(file)){
			GsonBuilder builder = new GsonBuilder().serializeSpecialFloatingPointValues()
					.enableComplexMapKeySerialization() 
					.registerTypeHierarchyAdapter(ViewId.class, new ViewIdJsonSerializer())
					.registerTypeHierarchyAdapter(AffineTransform3D.class, new AffineTransform3DJsonSerializer());
			Gson gson = builder.create();
			String json = gson.toJson(this);
			out.print(json);
			out.flush();
			out.close();
			System.out.println("File saved: "+file.getAbsolutePath()+" | Size: "+file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}


	public static FusionParams fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		GsonBuilder builder = new GsonBuilder().serializeSpecialFloatingPointValues()
				.enableComplexMapKeySerialization() 
				.registerTypeHierarchyAdapter(ViewId.class, new ViewIdJsonSerializer())
				.registerTypeHierarchyAdapter(AffineTransform3D.class, new AffineTransform3DJsonSerializer());
		Gson gson = builder.create();
		
		return gson.fromJson(new FileReader(file), FusionParams.class);
	}
	
	public String getXml() {
		return xml;
	}

	public FusionParams(String xml, Interval boundingBox, double downsampling, Map<ViewId, AffineTransform3D> registrations,
		Set<ViewDescription> views, boolean useBlending, boolean useContentBased, int interpolation, HashMap<ViewId, AffineModel1D> intensityAdjustment) {
	super();
	this.xml = xml;
	this.downsampling = downsampling;
	this.registrations = registrations;
	this.views = views;
	this.useBlending = useBlending;
	this.useContentBased = useContentBased;
	this.interpolation = interpolation;
	this.boundingBox = new BoundingBox(boundingBox);;
	this.intensityAdjustment = intensityAdjustment;
	
}
	
	public Map<ViewId, AffineTransform3D> getRegistrations() {
		return registrations;
	}

	public Set<ViewDescription> getViews() {
		return views;
	}
	
	public Interval getBoundingBox() {
		return boundingBox;
	}
	
	public HashMap<ViewId, AffineModel1D> getIntensityAdjustment() {
		return intensityAdjustment;
	}

	public boolean isUseBlending() {
		return useBlending;
	}

	public boolean isUseContentBased() {
		return useContentBased;
	}

	public int getInterpolation() {
		return interpolation;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}

	public double getDownsampling() {
		return downsampling;
	}

	public SpimData2 getSpimData() throws SpimDataException {
		return new XmlIoSpimData2( "" ).load(xml);
	}

}
