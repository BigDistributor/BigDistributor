package main.java.net.preibisch.distribution.io.img;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.io.img.n5.LoadN5;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxMaximal;


public class Loader {
	final File file;
	final SpimData2 spimData;
	final JFile jfile;

	public SpimData2 getSpimData() {
		return spimData;
	}
	public File getFile() {
		return file;
	}
	
	private Loader(File file, SpimData2 spimData, JFile jfile) {
		this.jfile = jfile;
		this.file = file;
		this.spimData = spimData;
	}
	
	public RandomAccessibleInterval< FloatType > fuse(final SpimData2 spimData, final List< ViewId > viewIds, final Interval interval )
	{
		switch (jfile.getExtension()) {
		case TIF:
			//TODO
			break;
		case XML:
			return LoadXML.fuse(spimData, viewIds, interval);

		default:
			break;
		}
		return null;
	}

	public RandomAccessibleInterval< FloatType > fuse() throws IncompatibleTypeException
	{
		switch (jfile.getExtension()) {
		case TIF:
//			return LoadTIFF.load(jfile.getAll());
//			main.java.com.tools.IOFunctions.openAs32Bit(new File(jfile.getAll()));
			return  LoadTIFF.load(jfile.getAll());
		case XML:
			final List<ViewId> viewIds = new ArrayList<>(spimData.getSequenceDescription().getViewDescriptions().values()); 
			final BoundingBox bb = new BoundingBoxMaximal( viewIds,spimData ).estimate( "Full Bounding Box" );
			return LoadXML.fuse(spimData, viewIds, bb);
		case N5:
			return LoadN5.fuse(jfile.getAll());
		default:
			break;
		}
		return null;
	}
	

	public static class Builder {
		public static Loader load(JFile jfile) {
			switch (jfile.getExtension()) {
			case TIF:
				return new Loader(null,null,jfile);
			case XML:
				final LoadXML loadxml = new LoadXML(jfile.getAll() );
				return new Loader(loadxml.file, loadxml.spimData,jfile);
			case N5:
				final LoadN5 loadn5 = new LoadN5(jfile.getAll());
				return new Loader(null,null, jfile);

			default:
				break;
			}
			return null;
		}
	}

}
