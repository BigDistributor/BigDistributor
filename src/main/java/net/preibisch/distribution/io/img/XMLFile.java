package main.java.net.preibisch.distribution.io.img;

import java.util.ArrayList;
import java.util.List;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxBigDataViewer;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxEstimation;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxMaximal;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class XMLFile extends ImgFile {
	private List<ViewId> viewIds;
	private BoundingBox bb;
	private SpimData2 spimData;
	private double downsampling;
//	private T dataType;

	public SpimData2 spimData() {
		return spimData;
	}

	public List<ViewId> viewIds() {
		return viewIds;
	}

	public BoundingBox bb() {
		return bb;
	}

	public XMLFile(String path) throws SpimDataException {
		this(path, false);
	}

	public XMLFile(String path, boolean useBDV) throws SpimDataException {
		this(path, Double.NaN, useBDV);
	}

	public XMLFile(String path, double downsampling, boolean useBDV) throws SpimDataException {
		super(path);
		spimData = new XmlIoSpimData2("").load(path);
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimData.getSequenceDescription().getViewDescriptions().values());
		bb = estimateBoundingBox(useBDV);
		this.downsampling = downsampling;
		this.dims = bb.getDimensions((int) downsampling);
//		dataType = Util.getTypeFromInterval(fuse());

	}

	private BoundingBox estimateBoundingBox(boolean useBDV) {
		BoundingBoxEstimation estimation;
		if (useBDV)
			estimation = new BoundingBoxBigDataViewer(spimData, viewIds);
		else
			estimation = new BoundingBoxMaximal(viewIds, spimData);

		return bb = estimation.estimate("Full Bounding Box");
	}

	@Override
	public RandomAccessibleInterval<FloatType> fuse() {
		return FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling);
	}

}