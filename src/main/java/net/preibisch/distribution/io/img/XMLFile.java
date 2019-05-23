package main.java.net.preibisch.distribution.io.img;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;

import main.java.net.preibisch.distribution.algorithm.controllers.items.DataExtension;
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
	private final static String HDF5_FILE = "dataset.h5";

	private List<ViewId> viewIds;
	private BoundingBox bb;
	private SpimData2 spimData;
	private double downsampling;
	private List<File> relatedFiles;
//	private T dataType;

	public List<File> getRelatedFiles() {
		return relatedFiles;
	}

	public SpimData2 spimData() {
		return spimData;
	}

	public List<ViewId> viewIds() {
		return viewIds;
	}

	public BoundingBox bb() {
		return bb;
	}

	public XMLFile(String path) throws SpimDataException, IOException {
		this(path, false);
	}

	public XMLFile(String path, boolean useBDV) throws SpimDataException, IOException {
		this(path, Double.NaN, useBDV);
	}

	public XMLFile(String path, double downsampling, boolean useBDV) throws SpimDataException, IOException {
		super(path);

		if (!exists())
			throw new IOException("File not found! " + path);

		if (!DataExtension.XML.equals(DataExtension.fromURI(path)))
			throw new IIOException("Invalide input! " + path);

		spimData = new XmlIoSpimData2("").load(path);

		this.relatedFiles = initRelatedFiles();
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimData.getSequenceDescription().getViewDescriptions().values());
		this.viewIds = viewIds;
		bb = estimateBoundingBox(useBDV);
		this.downsampling = downsampling;

		if (Double.isNaN(downsampling))
			this.dims = bb.getDimensions(1);
		else
			this.dims = bb.getDimensions((int) downsampling);

//		dataType = Util.getTypeFromInterval(fuse());
		System.out.println(toString());
	}

	private List<File> initRelatedFiles() throws IOException {
		List<File> files = new ArrayList<File>();
		File hdfFile = new File(getParent(), HDF5_FILE);
		if (!hdfFile.exists())
			throw new IOException("HDF5 file not exist: " + hdfFile.getAbsolutePath());
		files.add(hdfFile);
		return files;
	}

	@Override
	public String toString() {
		String related = "\n Related files: \n";
		for (File f : relatedFiles)
			related += f.getAbsolutePath() + "\n";

		return super.toString() + related;
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
	public RandomAccessibleInterval<FloatType> fuse() throws IOException {
		return FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling);
	}

	@Override
	public RandomAccessibleInterval<FloatType> fuse(BoundingBox bb) throws IOException {
		return FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling);
	}

}