package net.preibisch.bigdistributor.io.serializers.params;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;

public interface ParamsJsonSerializer<T> {
	public T fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException;
	public void toJson(File file);
}
