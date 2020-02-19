package net.preibisch.distribution.algorithm.task.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bdv.img.remote.AffineTransform3DJsonSerializer;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.distribution.io.serializers.viewid.ViewIdJsonSerializer;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;

public class ParamJsonHelpers {
	public static Gson getGson(){
		GsonBuilder builder = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization()
				.registerTypeHierarchyAdapter(ViewId.class, new ViewIdJsonSerializer())
				.registerTypeHierarchyAdapter(AffineTransform3D.class, new AffineTransform3DJsonSerializer());
		return builder.create();
	}
	
	protected SpimData2 getSpimData(String xml) throws SpimDataException {
		return new XmlIoSpimData2("").load(xml);
	}
}
