package main.java.net.preibisch.distribution.io.img.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.java.net.preibisch.distribution.algorithm.controllers.items.DataFile;
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


	public SpimData2 getSpimData() {
		return spimData;
	}
	public File getFile() {
		return file;
	}
	
	private Loader(File file, SpimData2 spimData, DataFile file) {
		this.file = file;
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

	public static RandomAccessibleInterval< FloatType > fuse(DataFile file) throws IncompatibleTypeException
	{
		switch (file.getExtension()) {
		case TIF:

			return  LoadTIFF.load(file.getAbsolutePath());
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
	

	public static Loader load(DataFile file) {
		switch (file.getExtension()) {
		case TIF:
			return new Loader(null,null,file);
		case XML:
			final LoadXML loadxml = new LoadXML(file.getAbsolutePath() );
			return new Loader(loadxml.file, loadxml.spimData,file);
		case N5:
			final LoadN5 loadn5 = new LoadN5(file.getAbsolutePath());
			return new Loader(null,null, file);

		default:
			break;
		}
		return null;
	}

}
