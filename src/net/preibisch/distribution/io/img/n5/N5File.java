package net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.Compression;
import org.janelia.saalfeldlab.n5.DataType;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.blockmanager.BlockConfig;
import net.preibisch.distribution.algorithm.controllers.items.DataExtension;
import net.preibisch.distribution.io.img.ImgFile;
import net.preibisch.distribution.tools.Tools;

public class N5File extends ImgFile {
	private final static Compression COMPRESSION = new GzipCompression();

	private final static DataType DATA_TYPE = DataType.FLOAT64;

	private static final String DEFAULT_DATASET = "/volumes/raw";

	private String dataset = "/volumes/raw";
	private int[] blocksize;

	public int[] getBlocksize() {
		return blocksize;
	}

	N5File(String path, int[] blocksize, long[] dims) throws IOException {
		this(path, DEFAULT_DATASET, blocksize, dims);
	}

	public N5File(String path, String dataset, int[] blocksize, long[] dims) throws IOException {
		super(path);
//		clean();
		this.dataset = dataset;
		this.blocksize = blocksize;
		this.dims = dims;
	}
	public N5File(String path, long[] dims) throws IOException {
		this(path, Tools.array(BlockConfig.BLOCK_UNIT, dims.length), dims);
	}
	public N5File(String path, long[] dims,int blockUnit) throws IOException {
		this(path, Tools.array(blockUnit, dims.length), dims);
	}

	public static N5File fromXML(ImgFile xmlFile, String path,int blockUnit) throws IOException {
		if (DataExtension.XML.equals(xmlFile.getExtension())) {
			long[] dims = xmlFile.getDims();
			return new N5File(path, dims,blockUnit);
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

	@Override
	public RandomAccessibleInterval<FloatType> fuse() throws IOException {
		N5Reader reader = new N5FSReader(getAbsolutePath());
		return N5Utils.open(reader, dataset);
	}

	private void clean() throws IOException {
		if (exists())
			Tools.deleteRecursively(this);
	}

	public static N5File open(String path) throws IOException {
		return new N5File(path, new long[] {0,0,0},32);
	}

}
