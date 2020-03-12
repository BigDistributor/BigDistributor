package net.preibisch.distribution.algorithm.task.params;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import bdv.img.remote.AffineTransform3DJsonSerializer;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.realtransform.AffineTransform3D;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.serializers.viewid.ViewIdJsonSerializer;
import net.preibisch.legacy.io.IOFunctions;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class SerializableParams {

	protected BoundingBox boundingBox;
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public static Gson getGson() {
		GsonBuilder builder = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization()
				.registerTypeHierarchyAdapter(ViewId.class, new ViewIdJsonSerializer())
				.registerTypeHierarchyAdapter(AffineTransform3D.class, new AffineTransform3DJsonSerializer());
		return builder.create();
	}

	public void toJson(File file) {
		try (PrintWriter out = new PrintWriter(file)) {
			String json = getGson().toJson(this);
			out.print(json);
			out.flush();
			out.close();
			IOFunctions.println("File saved: " + file.getAbsolutePath() + " | Size: " + file.length());
		} catch (IOException e) {
			MyLogger.log().error(e);
		}
	}
	
	public static  <T> T fromJson(File file,Class<T> cls) throws JsonSyntaxException, JsonIOException, FileNotFoundException  {
		return getGson().fromJson(new FileReader(file), cls);
	}

	protected static SpimData2 getSpimData(String input) throws SpimDataException {
		if (DataExtension.fromURI(input) != DataExtension.XML)
			throw new SpimDataException("input " + input + " is not XML");
		return new XmlIoSpimData2("").load(input);
	}
}
