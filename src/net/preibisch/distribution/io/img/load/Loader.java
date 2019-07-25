package net.preibisch.distribution.io.img.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.io.img.ImgFile;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxMaximal;

public class Loader {
	final ImgFile file;

	public ImgFile getFile() {
		return file;
	}

	private Loader(ImgFile file) {
		this.file = file;
	}

	public RandomAccessibleInterval<FloatType> fuse(final SpimData2 spimData, final List<ViewId> viewIds,
			final Interval interval) {
		switch (file.getExtension()) {
		case TIF:
			// TODO
			break;
		case XML:
			return LoadXML.fuse(spimData, viewIds, interval);

		default:
			break;
		}
		return null;
	}

	public static RandomAccessibleInterval<FloatType> fuse(ImgFile file) throws IncompatibleTypeException, IOException {
		switch (file.getExtension()) {
		case TIF:

			return LoadTIFF.load(file.getAbsolutePath());
		case XML:
			XMLFile xmlfile = (XMLFile) file;
			final List<ViewId> viewIds = new ArrayList<>(
					xmlfile.spimData().getSequenceDescription().getViewDescriptions().values());
			final BoundingBox bb = new BoundingBoxMaximal(viewIds, xmlfile.spimData()).estimate("Full Bounding Box");
			return LoadXML.fuse(xmlfile.spimData(), viewIds, bb);
		case N5:
			N5File n5file = (N5File) file;
			return n5file.fuse();
		default:
			break;
		}
		return null;
	}

	public static Loader load(ImgFile file) {
		return new Loader(file);
	}
}
