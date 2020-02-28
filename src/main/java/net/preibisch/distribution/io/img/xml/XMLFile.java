package net.preibisch.distribution.io.img.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.img.ImgFile;
import net.preibisch.distribution.io.img.ImgFunctions;
import net.preibisch.distribution.tools.helpers.IOHelpers;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxEstimation;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxMaximal;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class XMLFile<T extends FloatType> extends ImgFile  implements ImgFunctions<T> {
	private final static String HDF5_FILE = "dataset.h5";

	private List<List<ViewId>> viewIds;
	private Interval bb;
	private SpimData2 spimData;
	private double downsampling;
	private List<File> relatedFiles;


	public List<File> getRelatedFiles() {
		return relatedFiles;
	}

	public SpimData2 spimData() {
		return spimData;
	}

	public List<List<ViewId>> viewIds() {
		return viewIds;
	}

	public Interval bb() {
		return bb;
	}
	
	public long[] getDimensions(int downsampling) {
		return new BoundingBox(bb).getDimensions(downsampling);
	}

	public static XMLFile XMLFile(String path) throws SpimDataException, IOException {
		return XMLFile(path, Double.NaN);
	}

	public static XMLFile XMLFile(String path, double downsampling)
			throws SpimDataException, IOException {
		File f = new File(path);
		if (!f.exists())
			throw new IOException("File not found! " + path);

		if (!DataExtension.XML.equals(DataExtension.fromURI(path)))
			throw new IIOException("Invalide input! " + path);
		System.out.println("File found! ");

		SpimData2 spimdata = new XmlIoSpimData2("").load(path);

		List<File> relatedFiles = initRelatedFiles(f);
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimdata.getSequenceDescription().getViewDescriptions().values());
		BoundingBox bbx = estimateBoundingBox(spimdata, viewIds);
		int down;
		if (Double.isNaN(downsampling))
			down = 1;
		else
			down = (int) downsampling;
		return new XMLFile(path, bbx, down, list(viewIds), relatedFiles);

	}

	public static XMLFile XMLFile(String path, Interval bb, double downsampling) throws IOException, SpimDataException {
		final List<ViewId> viewIds = new ArrayList<ViewId>();
		SpimData2 spimdata = new XmlIoSpimData2("").load(path);
		viewIds.addAll(spimdata.getSequenceDescription().getViewDescriptions().values());
		return XMLFile(path, bb,  downsampling, viewIds);
	}
	
	public static XMLFile XMLFile(String path, Interval bb, double downsampling, List<ViewId> viewIds) throws IOException, SpimDataException {

		System.out.println("XML specific info loader ");
		File f = new File(path);
		if (!f.exists())
			throw new IOException("File not found! " + path);

		if (!DataExtension.XML.equals(DataExtension.fromURI(path)))
			throw new IIOException("Invalide input! " + path);
		System.out.println("File found! ");

		SpimData2 spimdata = new XmlIoSpimData2("").load(path);

		List<File> relatedFiles = initRelatedFiles(f);

		return new XMLFile(path, bb, downsampling,  list(viewIds), relatedFiles);
	}


//	public XMLFile(String path, BoundingBox bb, SpimData2 spimdata, int downsampling, List<ViewId> viewIds,
//			List<File> relatedFiles) {
//		this(path,bb,spimdata,downsampling,list(viewIds),relatedFiles);
//	}
	
	private static List<List<ViewId>> list(List<ViewId> viewIds) {
		List<List<ViewId>> l = new ArrayList<>();
		l.add(viewIds);
		return l;
	}

	public XMLFile(String path, Interval bb, double downsampling, List<List<ViewId>> viewIds,
			List<File> relatedFiles) throws SpimDataException {
		super(path);

		this.viewIds = viewIds;
		this.bb = bb;
		this.downsampling = downsampling;
		this.spimData =  new XmlIoSpimData2( "" ).load(path);
		this.relatedFiles = relatedFiles;
		int down = ( Double.isNaN(downsampling))?1:(int)downsampling;
		System.out.println("Down "+downsampling+"->"+down);
		this.dims = new BoundingBox(bb).getDimensions(down);

		// dataType = Util.getTypeFromInterval(fuse());
		System.out.println(toString());
	}

	public static List<File> initRelatedFiles(File f) throws IOException {
		List<File> files = new ArrayList<File>();
		File hdfFile = new File(f.getParent(), HDF5_FILE);
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

	private static BoundingBox estimateBoundingBox(SpimData2 spimdata, List<ViewId> viewIds) {
		BoundingBoxEstimation estimation = new BoundingBoxMaximal(viewIds, spimdata);
		return estimation.estimate("Full Bounding Box");
	}

	public RandomAccessibleInterval<FloatType> fuse(int i) throws IOException {
		return FusionTools.fuseVirtual(spimData, viewIds.get(i), bb, downsampling).getA();
	}

	public RandomAccessibleInterval<FloatType> fuse(Interval bb,int i) throws IOException {
		return FusionTools.fuseVirtual(spimData, viewIds.get(i), bb, downsampling).getA();
	}

	public static String fromStitchFolder(String inputPath) {
		return IOHelpers.getXML(inputPath);
	}

	@Override
	public RandomAccessibleInterval<FloatType> getImg() throws IOException {
		return fuse(0);
	}

}