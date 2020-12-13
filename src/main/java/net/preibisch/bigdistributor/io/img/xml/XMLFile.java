package net.preibisch.bigdistributor.io.img.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.IIOException;

import bdv.BigDataViewer;
import bdv.util.BehaviourTransformEventHandlerPlanar.BehaviourTransformEventHandlerPlanarFactory;
import bdv.viewer.ViewerOptions;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.generic.sequence.BasicViewDescription;
import mpicbg.spim.data.sequence.ViewDescription;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.bigdistributor.tools.helpers.IOHelpers;
import net.preibisch.bigdistributor.io.DataExtension;
import net.preibisch.bigdistributor.io.FileStatus;
import net.preibisch.bigdistributor.io.img.ImgFile;
import net.preibisch.bigdistributor.io.img.ImgFunctions;
import net.preibisch.helpers.fromstitchergui.BDVPopup;
import net.preibisch.helpers.fromstitchergui.MaximumProjectorARGB;
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
	public static XMLFile create(String path, double downsampling)
			throws SpimDataException, IOException {
		return create(path, downsampling, FileStatus.IN_LOCAL_COMPUTER);
	}

	public static XMLFile create(String path, double downsampling,FileStatus mode)
			throws SpimDataException, IOException {
		if (!DataExtension.XML.equals(DataExtension.fromURI(path)))
			throw new IIOException("Invalide input! " + path);
		
		MyLogger.log().info("File found :"+path);

		SpimData2 spimdata = new XmlIoSpimData2("").load(path);

		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimdata.getSequenceDescription().getViewDescriptions().values());
		BoundingBox bbx = estimateBoundingBox(spimdata, viewIds);
		int down;
		if (Double.isNaN(downsampling))
			down = 1;
		else
			down = (int) downsampling;
		return new XMLFile(path, bbx, down, list(viewIds));
	}

	private static List<List<ViewId>> list(List<ViewId> viewIds) {
		List<List<ViewId>> l = new ArrayList<>();
		l.add(viewIds);
		return l;
	}

	public XMLFile(String path, Interval bb, double downsampling, List<List<ViewId>> viewIds) throws SpimDataException, IOException {
		super(path);
		this.viewIds = viewIds;
		this.bb = bb;
		this.downsampling = downsampling;
		this.spimData =  new XmlIoSpimData2( "" ).load(path);
		this.relatedFiles.add(new File(getParent(), HDF5_FILE));
		int down = ( Double.isNaN(downsampling))?1:(int)downsampling;
		System.out.println("Down "+downsampling+"->"+down);
		this.dims = new BoundingBox(bb).getDimensions(down);
		System.out.println(toString());
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

	public BigDataViewer show(String title) throws IOException {
		boolean allViews2D = true;
		 Collection<ViewDescription> viewDescriptions = spimData.getSequenceDescription().getViewDescriptions().values();
		for (final BasicViewDescription< ? > vd : viewDescriptions)
			if (vd.isPresent() && vd.getViewSetup().hasSize() && vd.getViewSetup().getSize().dimension( 2 ) != 1)
			{
				allViews2D = false;
				break;
			}
		final ViewerOptions options = ViewerOptions.options().accumulateProjectorFactory( MaximumProjectorARGB.factory );
		if (allViews2D)
		{
			options.transformEventHandlerFactory(new BehaviourTransformEventHandlerPlanarFactory() );
		}
		BigDataViewer bdv = BigDataViewer.open( spimData,"BigDataViewer", null, options );
		BDVPopup.initTransform( bdv.getViewer() );	
		BDVPopup.initBrightness( 0.001, 0.999, bdv.getViewer().getState(), bdv.getSetupAssignments() );
		return bdv;
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
	
	protected SpimData2 getSpimData(String input) throws SpimDataException {
		if (DataExtension.fromURI(input) != DataExtension.XML)
			throw new SpimDataException("input " + input + " is not XML");
		return new XmlIoSpimData2("").load(input);
	}
	
	@Deprecated
	public static XMLFile XMLFile(String path) throws SpimDataException, IOException {
		return  create(path, Double.NaN);
	}
}