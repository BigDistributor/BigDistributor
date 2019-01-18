package main.java.com.controllers.tasks;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import main.java.com.controllers.items.callback.AbstractCallBack;

public class DirManager {
	public static void cleanFolder(String dir, AbstractCallBack callback) {
		try {
			FileUtils.cleanDirectory(new File(dir));
		} catch (IOException e) {
			callback.onError(e.toString());
		}
		callback.onSuccess(0);
	}

}
