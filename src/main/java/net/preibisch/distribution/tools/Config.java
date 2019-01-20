package main.java.net.preibisch.distribution.tools;

import java.util.ArrayList;

import com.jcraft.jsch.Session;

import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public enum Config {
	INSTANCE;
	
	public static final int BUFFER_SIZE = 64 * 1024;
	public static final int MINIMUM_BOX_SIZE = 50;
	public static final int PREVIEW_PANEL_WIDTH = 800;
	public static final int PREIVIEW_PANEL_HEIGHT = 500;

	public static int previewPreferedHeight = 500;

	private static Login login;
	private static Job job;

	private static DataPreview dataPreview;
	
//	private static String clusterPath = "/fast/AG_Preibisch/Marwan/clustering/";
	private static String defaultInputPath = "/Users/Marwan/Desktop/Task";

	private static int blocks;
	

	private static Session currentSession;

	private static int[] numberBlocks = {};
	private static int[] blocksStatus;


	
	private static int totalInputFiles;

	private static String tempFolderPath = "/Users/Marwan/Desktop/Task/";

	
	private static ArrayList<String> blocksFilesNames;
	private static String inputPrefix = ".tif";
	public static Img<FloatType> resultImg;
	public static int parallelJobs;
	public static Img<FloatType> resultImage;

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

	
	public static int[] getNumberBlocks() {
		return numberBlocks;
	}

	public static void setNumberBlocks(int[] numberBlocks) {
		Config.numberBlocks = numberBlocks;
	}


	public static Session getSession() {
		return currentSession;
	}

	public static void setSession(Session session) {
		Config.currentSession = session;
	}
	

		public static void setLogin(Login login) {
		Config.login = login;

	}
	
	public static Login getLogin() {
		return Config.login;
	}
	
	public static void setJob(Job job) {
		Config.job = job;
	}
	
	public static Job getJob() {
		return Config.job;
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

	public static DataPreview getDataPreview() {
		return dataPreview;
	}

	public static void setDataPreview(DataPreview dataPreview) {
		Config.dataPreview = dataPreview;
	}




}
