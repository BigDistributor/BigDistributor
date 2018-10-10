package main.java.com.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import com.jcraft.jsch.Session;

import main.java.com.gui.items.BlockView;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public enum Config {
	INSTANCE;
	
	public static AppMode APP_MODE = AppMode.LocalInputMode;
	public static final int BUFFER_SIZE = 64 * 1024;
	public static final int MINIMUM_BOX_SIZE = 50;
	public static final int PREVIEW_PANEL_WIDTH = 800;
	public static final int PREIVIEW_PANEL_HEIGHT = 500;

	public static int previewPreferedHeight = 500;

	private static String pseudo = "mzouink";
	private static String pw = "";
	private static String host = "maxlogin2.mdc-berlin.net";
	private static int port = 22;
	private static String clusterPath = "/fast/AG_Preibisch/Marwan/clustering/";
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
	private static String uuid;
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

	public static String getClusterPath() {
		return clusterPath;
	}

	public static void setClusterPath(String clusterPath) {
		Config.clusterPath = clusterPath;
	}

	public static Session getSession() {
		return currentSession;
	}

	public static void setSession(Session session) {
		Config.currentSession = session;
	}

	public static String getPseudo() {
		return pseudo;
	}

	public static void setPseudo(String pseudo) {
		Config.pseudo = pseudo;
	}

	public static String getPw() {
		return pw;
	}

	public static void setPw(String pw) {
		Config.pw = pw;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		Config.host = host;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Config.port = port;
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

	public static void config(String host, int port, String pseudo, String pw) {
		Config.host = host;
		Config.port = port;
		Config.pseudo = pseudo;
		Config.pw = pw;
		System.out.println("Got Config: " + Config.host);
	}

//	public static void addScriptFile(String scriptFile) {
//		if (scriptFiles == null) {
//			scriptFiles = new ArrayList<String>();
//		}
//		scriptFiles.add(scriptFile);
//	}

	public static String getClusterInput() {
		return clusterPath;
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
		Config.setUUID(UUID.randomUUID().toString().replace("-", ""));
		System.out.println("UUID:"+Config.getUUID());
		if(APP_MODE==AppMode.LocalInputMode) {
		Img<FloatType> image = IOFunctions.openAs32Bit(new File(Config.getOriginalInputFilePath()));
		setInputFile(image);
		setDimensions(Helper.getDimensions(image));
		}else if (APP_MODE==AppMode.ClusterInputMode) {
			setDimensions(new long[]{200,200});
		}
		
		setBlocksSize(200);
	}


	public static String getUUID() {
		return uuid;
	}

	public static void setUUID(String uuid) {
		Config.uuid = uuid;
	}

}
