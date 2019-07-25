package net.preibisch.distribution.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ij.ImageJ;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;

public class test {

	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
public static void main(String[] args) throws IOException, SpimDataException {
	String path = "/home/mzouink/Desktop/testn5/back_ output45.n5";


	new ImageJ();
	MyLogger.initLogger();

	new Job();

	// Input XML
	XMLFile inputFile = XMLFile.XMLFile(input_path);

	String outputPath = Job.file("output.n5").getAbsolutePath();
	// Generate Metadata
	N5File outputFile = new N5File(outputPath, inputFile.getDims());
	System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));

	// create output
	outputFile.create();

	Files.walk(Paths.get(outputPath )).forEach(System.out::println);
}
}
