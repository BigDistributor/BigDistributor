package net.preibisch.distribution.algorithm.task.params;

import java.io.File;
import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public interface ParamsJsonSerialzer<T> {
	public T fromJson(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException;
	public void toJson(File file);
}
