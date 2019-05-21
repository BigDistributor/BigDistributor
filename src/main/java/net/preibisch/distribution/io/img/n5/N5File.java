package main.java.net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.Compression;
import org.janelia.saalfeldlab.n5.DataType;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.RawCompression;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import main.java.net.preibisch.distribution.io.img.ImgFile;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import main.java.net.preibisch.distribution.tools.Tools;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class N5File extends ImgFile {
	private final static Compression COMPRESSION = new RawCompression();
	private final static int BLOCK_UNIT = 32;
	private final static DataType DATA_TYPE = DataType.FLOAT64;
	
	private String dataset = "/volumes/raw";
	private int[] blocksize;

	N5File(String path, int[] blocksize, long[] dims) {
		this(path, "/volumes/raw", blocksize, dims);
	}

	public N5File(String path, String dataset, int[] blocksize, long[] dims) {
		super(path);
		this.dataset = dataset;
		this.blocksize = blocksize;
		this.dims = dims;
	}

	public N5File(String path, long[] dims) {
		this(path, Tools.array(BLOCK_UNIT, dims.length), dims);
	}

	public static N5File fromXML(XMLFile xmlFile, String path) {
		long[] dims = xmlFile.getDims();

		return new N5File(path, dims);
	}

	public void create() throws IOException {
		N5Writer n5 = new N5FSWriter(getAbsolutePath());
		final DatasetAttributes attributes = new DatasetAttributes(dims, blocksize,
				DATA_TYPE, COMPRESSION);
		n5.createDataset(dataset, attributes);
		System.out.println("dataset created : " + getAbsolutePath());
	}

	public void saveBlock(RandomAccessibleInterval<FloatType> block, long[] gridOffset) throws IOException {
		N5Writer n5 = new N5FSWriter(getAbsolutePath());
		N5Utils.saveBlock(block, n5, dataset, gridOffset);
	}
	
	public RandomAccessibleInterval<FloatType> open() throws IOException{
		N5Reader reader = new N5FSReader(getAbsolutePath());
		return N5Utils.open(reader , dataset);
	}
	

}
