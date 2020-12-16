package net.preibisch.bigdistributor.tools.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import net.preibisch.bigdistributor.io.DataExtension;

public class IOHelpers {
	public static ArrayList<String> getFiles(String path, String prefix) {
		File folder = new File(path);
		String[] files = folder.list();
		ArrayList<String> result = new ArrayList<>();
		for (String file : files) {
			if (file.endsWith(prefix))
				result.add(file);
		}
		return result;
	}

	public static void deleteRecursively(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteRecursively(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	public static void cleanFolder(String dir) throws IOException {
		File folder = new File(dir);
		FileUtils.deleteDirectory(folder);
		folder.mkdir();
	}

	
	public static String getXML(String inputPath) {
		if (new File(inputPath).isFile())
			return inputPath;
		File folder = new File(inputPath.substring(0, inputPath.length() - 2));
		for (File f : folder.listFiles()) {
			if (DataExtension.XML.equals(DataExtension.fromURI(f.getName()))) {
				return f.getAbsolutePath();
			}
		}
		return null;
	}
}
