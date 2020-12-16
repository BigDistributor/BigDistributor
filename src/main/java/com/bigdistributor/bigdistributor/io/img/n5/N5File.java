package net.preibisch.bigdistributor.io.img.n5;

import java.io.IOException;

import net.preibisch.bigdistributor.algorithm.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import net.preibisch.bigdistributor.tools.helpers.ArrayHelpers;
import net.preibisch.bigdistributor.tools.helpers.IOHelpers;
import org.janelia.saalfeldlab.n5.Compression;
import org.janelia.saalfeldlab.n5.DataType;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import bdv.util.BdvStackSource;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.bigdistributor.io.DataExtension;
import net.preibisch.bigdistributor.io.FileStatus;
import net.preibisch.bigdistributor.io.img.ImgFile;
import net.preibisch.mvrecon.process.export.DisplayImage;

public class N5File extends ImgFile {
	private final static Compression COMPRESSION = new GzipCompression();

	private final static DataType DATA_TYPE = DataType.FLOAT64;

	private static final String DEFAULT_DATASET = "/volumes/raw";

	private String dataset = "/volumes/raw";
	private int[] blocksize;

	public int[] getBlocksize() {
		return blocksize;
	}

	N5File(String path, long[] dims, int[] blocksize) throws IOException {
		this(path, DEFAULT_DATASET, dims, blocksize);
	}

	public N5File(String path, String dataset, long[] dims, int[] blocksize) throws IOException {
		super(path,FileStatus.IN_CLUSTER);
		// clean();
		this.dataset = dataset;
		this.blocksize = blocksize;
		this.dims = dims;
	}

	public N5File(String path, long[] dims) throws IOException {
		this(path, dims, ArrayHelpers.array((int) BasicBlockInfoGenerator.BLOCK_SIZE, dims.length));
	}

	public N5File(String path, long[] dims, int blockUnit) throws IOException {
		this(path, dims, ArrayHelpers.array(blockUnit, dims.length));
	}


	public static N5File fromXML(ImgFile xmlFile, String path, int blockUnit) throws IOException {
		if (DataExtension.XML.equals(xmlFile.getExtension())) {
			long[] dims = xmlFile.getDims();
			return new N5File(path, dims, blockUnit);
		} else {
			throw new IOException("Input not XML File");
		}
	}

	public void create() throws IOException {
		N5Writer n5 = new N5FSWriter(getAbsolutePath());
		final DatasetAttributes attributes = new DatasetAttributes(dims, blocksize, DATA_TYPE, COMPRESSION);
		n5.createDataset(dataset, attributes);
		System.out.println("dataset created : " + getAbsolutePath());
	}

	public void saveBlock(RandomAccessibleInterval<FloatType> block, long[] gridOffset) throws IOException {
		N5Writer n5 = new N5FSWriter(getAbsolutePath());
		N5Utils.saveBlock(block, n5, dataset, gridOffset);
	}

	public RandomAccessibleInterval<FloatType> fuse() throws IOException {
		N5Reader reader = new N5FSReader(getAbsolutePath());
		return N5Utils.open(reader, dataset);
	}

	public void clean() throws IOException {
		if (exists())
			IOHelpers.deleteRecursively(this);
	}
	
	public RandomAccessibleInterval<FloatType> getImg() throws IOException {
		return N5IO.read(getAbsolutePath());
	}
	public BdvStackSource<FloatType> show(String title) throws IOException {
		RandomAccessibleInterval<FloatType> img = N5IO.read(getAbsolutePath());
		DisplayImage.getImagePlusInstance( img, false, "Fused", 0, 255 ).show();
//		ImageJFunctions.show(img,title);
//		BdvStackSource<FloatType> bdv = BdvFunctions.show(img, title);
//		bdv.setCurrent();
		return null;
//
//		bdv.getBdvHandle().get
//		BDVPopup.initTransform( bdv.getViewer() );	
//		BDVPopup.initBrightness( 0.001, 0.999, bdv.getViewer().getState(), bdv.getSetupAssignments() );

	}

	public static N5File open(String path) throws IOException {
		return new N5File(path, new long[] { 0, 0, 0 }, 32);
	}

}
