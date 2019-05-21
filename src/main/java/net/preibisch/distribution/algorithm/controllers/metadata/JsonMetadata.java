package main.java.net.preibisch.distribution.algorithm.controllers.metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class JsonMetadata {
	
	public static void createJSon(BlocksMetaData md, File file, AbstractCallBack callback) throws IOException {
		Writer writer = new FileWriter(file);
		Gson gson = new GsonBuilder().create();
		gson.toJson(md, writer);
	}

}
