package main.java.com.clustering.workflow;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import main.java.com.clustering.MyCallBack;

public class DirManager {
	public static void cleanFolder(String dir, MyCallBack callback) {
		try {
			FileUtils.cleanDirectory(new File(dir));
		} catch (IOException e) {
			callback.onError(e.toString());
		}
		callback.onSuccess();
	}

}
