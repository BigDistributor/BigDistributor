package main.java.net.preibisch.distribution.input.imageaccess;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class LoadN5 {
	final File file;
	final N5FSReader n5Reader;

	public LoadN5(final String n5path) {
		this.file = new File(n5path);

		if (!file.exists())
			throw new RuntimeException("File " + n5path + " does not exist.");
		try {
			n5Reader = new N5FSReader("/home/saalfeld/projects/lauritzen/02/workspace.n5");
			System.out.println(Arrays.toString(n5Reader.list("/")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new RuntimeException("Could not load n5: " + e);
		}

	}

	public RandomAccessibleInterval<FloatType> fuse() {
		RandomAccessibleInterval<FloatType> virtual = null;
		try {
			virtual = N5Utils.open(n5Reader, "volumes/raw/s0");
			return virtual;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return virtual;

	}

}
