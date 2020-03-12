package net.preibisch.distribution.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;

public class GsonIO {

	public static <T> T fromJson(String path, Class<T> cls)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		
		File f = new File(path);
		if(f.exists() & f.isFile())
			return new Gson().fromJson(new FileReader(path), cls);
		else
			throw new FileNotFoundException(f+" not found");
	}

	public static void toJson(Object obj, String path) {
		toJson(obj, new File(path));
	}

	public static void toJson(Object obj, File file) {
		try (PrintWriter out = new PrintWriter(file)) {
			Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
			String json = gson.toJson(obj);
			out.print(json);
			out.flush();
			out.close();
			MyLogger.log().info("File saved: " + file.getAbsolutePath() + " | Size: " + file.length());
		} catch (IOException e) {
			MyLogger.log().error(e);
		}
	}
	
	public static String toString(Object obj) {
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
		String json = gson.toJson(obj);
		return json;	
	}
}
