package main.java.net.preibisch.distribution.tools.config;

import java.util.ArrayList;

import com.jcraft.jsch.Session;

import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public enum Config {
	INSTANCE;

	private static DataPreview dataPreview;
	
	private static String defaultInputPath = "/Users/Marwan/Desktop/Task";

	private static int[] numberBlocks = {};
	private static int[] blocksStatus;


	private static int totalBlocks;
	
	private static ArrayList<String> blocksFilesNames;
	private static String inputPrefix = ".tif";
	public static Img<FloatType> resultImg;
	public static int parallelJobs;
	public static Img<FloatType> resultImage;


	public static int[] getBlocksStatus() {
		return blocksStatus;
	}

	public static void setBlocksStatus(int[] blocksStatus) {
		Config.blocksStatus = blocksStatus;
	}

	public static String getDefaultInputPath() {
		return defaultInputPath;
	}

	public static void setDefaultInputPath(String defaultInputPath) {
		Config.defaultInputPath = defaultInputPath;
	}
	
	
	public static int[] getNumberBlocks() {
		return numberBlocks;
	}

	public static void setNumberBlocks(int[] numberBlocks) {
		Config.numberBlocks = numberBlocks;
	}

	public static ArrayList<String> getBlocksFilesNames() {
		return blocksFilesNames;
	}

	public static void setBlocksFilesNames(ArrayList<String> blocksFilesNames) {
		Config.blocksFilesNames = blocksFilesNames;
	}

	public static String getInputPrefix() {
		return inputPrefix;
	}

	public static void setInputPrefix(String inputPrefix) {
		Config.inputPrefix = inputPrefix;
	}

	public static int getTotalBlocks() {
		return totalBlocks;
	}

	public static void setTotalBlocks(int totalBlocks) {
		Config.totalBlocks = totalBlocks;
	}

	public static DataPreview getDataPreview() {
		return dataPreview;
	}

	public static void setDataPreview(DataPreview dataPreview) {
		Config.dataPreview = dataPreview;
	}




}
