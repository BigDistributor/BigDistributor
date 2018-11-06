package main.java.com.tools;

import java.io.File;
import java.util.ArrayList;

import com.jcraft.jsch.Session;

import main.java.com.gui.items.BlockView;
import main.java.com.tools.server.Login;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public enum Config {
	INSTANCE;
	
	public static AppMode APP_MODE = AppMode.ClusterInputMode;
	public static final int BUFFER_SIZE = 64 * 1024;
	public static final int MINIMUM_BOX_SIZE = 50;
	public static final int PREVIEW_PANEL_WIDTH = 800;
	public static final int PREIVIEW_PANEL_HEIGHT = 500;

	public static int previewPreferedHeight = 500;

	private static Login login;
	
//	private static String clusterPath = "/fast/AG_Preibisch/Marwan/clustering/";
	private static String defaultInputPath = "/Users/Marwan/Desktop/Task";

	private static int blocks;
	private static String path = "";
	

	private static String localTaskPath = "/Users/Marwan/Desktop/Task/GaussianTask.jar";
	private static String clusterTaskPath;

	private static String originalInputFilePath = "/Users/Marwan/Desktop/Task/DrosophilaWing.tif";

	private static Session currentSession;

	private static int[] numberBlocks = {};
	private static int[] blocksStatus;
	private static long[] dimensions;


	private static int overlap = 5;
	private static long[] blocksSize;
	
	private static int totalInputFiles;

	private static String tempFolderPath = "/Users/Marwan/Desktop/Task/";

	private static Img<FloatType> inputFile;
	public static ArrayList<BlockView> blocksView;

	public static long totalBlocks = 0;
	
	private static ArrayList<String> blocksFilesNames;
	private static String inputPrefix = ".tif";
	public static Img<FloatType> resultImg;
	public static int parallelJobs;

	public static int getBlocks() {
		return blocks;
	}

	public static void setBlocks(int blocks) {
		Config.blocks = blocks;
	}

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

	public static String getTempFolderPath() {
		return tempFolderPath;
	}

	public static void setTempFolderPath(String tempDir) {
		System.out.println("Temp Dir: "+tempDir);
		Config.tempFolderPath = tempDir;
	}

	public static long[] getBlocksSize() {
		return blocksSize;
	}

	public static long getBlockSize(int i) {
		return blocksSize[i];
	}

	public static void setBlocksSize(long[] blocksSize) {
		Config.blocksSize = blocksSize;
	}

	public static void setBlocksSize(long size, int position) {
		Config.blocksSize[position] = size;
	}

	public static int[] getNumberBlocks() {
		return numberBlocks;
	}

	public static void setNumberBlocks(int[] numberBlocks) {
		Config.numberBlocks = numberBlocks;
	}

	public static String getClusterTaskPath() {
		return clusterTaskPath;
	}

	public static void setClusterTaskPath(String clusterTaskPath) {
		Config.clusterTaskPath = clusterTaskPath;
	}

	public static String getLocalTaskPath() {
		return localTaskPath;
	}

	public static void setLocalTaskPath(String path) {
		Config.localTaskPath = path;
	}

	public static String getOriginalInputFilePath() {
		return originalInputFilePath;
	}

	public static void setOriginalInputFilePath(String path) {
		Config.originalInputFilePath = path;
	}

	public static int getOverlap() {
		return overlap;
	}

	public static void setOverlap(int overlap) {
		Config.overlap = overlap;
	}

	public static Session getSession() {
		return currentSession;
	}

	public static void setSession(Session session) {
		Config.currentSession = session;
	}
	
	

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Config.path = path;
	}

	public static Img<FloatType> getInputFile() {
		return inputFile;
	}

	public static void setInputFile(Img<FloatType> inputFile) {
		Config.inputFile = inputFile;
	}

	public static long[] getDimensions() {
		return dimensions;
	}

	public static void setDimensions(long[] dimensions) {
		Config.dimensions = dimensions;
	}

	public static void setLogin(Login login) {
		Config.login = login;

	}
	
	public static Login getLogin() {
		return Config.login;
	}




	public static void setBlocksSize(int value) {
		blocksSize = new long[dimensions.length];
		for (int i = 0; i < blocksSize.length; i++) {
			blocksSize[i] = value;
		}
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

	public static int getTotalInputFiles() {
		return totalInputFiles;
	}

	public static void setTotalInputFiles(int totalInputFiles) {
		Config.totalInputFiles = totalInputFiles;
	}

	public static void init() {
//		Config.setUUID(UUID.randomUUID().toString().replace("-", ""));
//		System.out.println("UUID:"+Config.getUUID());
		Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getOriginalInputFilePath()));
		setInputFile(image);
		setDimensions(Helper.getDimensions(image));
		setBlocksSize(200);
	}


}
