package net.preibisch.distribution.algorithm.task.params;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.preibisch.distribution.algorithm.controllers.items.ViewIdMD;
import net.preibisch.distribution.io.GsonIO;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.interestpointregistration.pairwise.constellation.grouping.Group;

public class FusionParams{
	private String xml;
	private List<List<ViewIdMD>> viewIds;
	private BoundingBox bb;
	private int downsampling;

	public void toJson(String path)  {
		if (Double.isNaN(this.downsampling))
			this.downsampling = 0;

		System.out.println(this.toString());
		GsonIO.toJson(this, path);

	}
	public List<List<ViewIdMD>> getViewMDIds() {
		return viewIds;
	}
	
	public List<List<ViewId>> getViewIds() {
		List<List<ViewId>> ids = new ArrayList<>();
		for(List<ViewIdMD> mds : viewIds) {
			List<ViewId> tmp = new ArrayList<>();
			for(ViewIdMD md: mds) {
				tmp.add(md.toViewId());
			}
			ids.add(tmp);
		}
		return ids;
	}
	
	public FusionParams(String xml, List<Group<ViewDescription>> list, BoundingBox bb, double downsampling) {
		super();
		this.xml = xml;
		this.viewIds = viewsID(list);
		this.bb = bb;
		this.downsampling  = ((int)downsampling==0) ? 1 : (int) downsampling;
	}

	private List<List<ViewIdMD>> viewsID(List<Group<ViewDescription>> list) {
		List<List<ViewIdMD>> groups = new ArrayList<>();
		for(Group<ViewDescription> views:list) {
			List<ViewId> viewIds = new ArrayList<>(views.getViews());
			List<ViewIdMD> ids = new ArrayList<ViewIdMD>();
			for(ViewId id: viewIds)
				ids.add(new ViewIdMD(id));
			groups.add(ids);
		}
		return groups;
	}


	public static FusionParams fromJson(String path) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		return new Gson().fromJson(new FileReader(path), FusionParams.class);
//		if (params.getDownsampling() == 0)
//			params.setDownsampling(Double.NaN);
//		return params;
	}

	@Override
	public String toString() {
		return "xml: "+xml+" | views: "+viewIds.size()+" | bb: "+bb+" | downsampling: "+downsampling;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Interval getBb() {
		return bb;
	}

	public void setBb(BoundingBox bb) {
		this.bb = bb;
	}

	public double getDownsampling() {
		return downsampling;
	}

	public void setDownsampling(int downsampling) {
		this.downsampling = downsampling;
	}

	public SpimData2 getSpimData() throws SpimDataException {
		return new XmlIoSpimData2( "" ).load(xml);
	}

}
