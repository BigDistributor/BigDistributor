package tools;

import java.io.File;
import java.util.ArrayList;

import com.jcraft.jsch.Session;

import gui.items.BlockView;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public enum Config {
	INSTANCE; 
	
	public static int bufferSize = 64*1024;
	private static String pseudo = "mzouink";
	private static String pw = "";
	private static String host = "maxlogin2.mdc-berlin.net";
	private static int port = 22;
	private static int blocks;
	private static String path = "";
	private static Boolean configured = false;
	private static ArrayList<String> scriptFiles;
	private static String clusterPath = "/fast/AG_Preibisch/Marwan/clustering/";
	private static String localJar;
	private static String clusterJar;
	private static String originalInputFilePath;
	private static Session session;
	private static int[] numberBlocks = {3,5};
	private static int[] blocksStatus;
	public static int progressValue = 0;
	public static ArrayList<String> log;
 	private static int overlap = 5; 
 	private static long blockSize = 50;
 	private static String inputTempDir;
 	private static String defaultInputPath = "/Users/Marwan/Desktop/Task";
 	private static Img<FloatType> inputFile;
	public static ArrayList<BlockView> blocksView;
	
 	
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
	public static String getInputTempDir() {
		return inputTempDir;
	}
	public static void setInputTempDir(String inputTempDir) {
		Config.inputTempDir = inputTempDir;
	}
	public static long getBlockSize() {
		return blockSize;
	}
	public static void setBlockSize(long blockSize) {
		Config.blockSize = blockSize;
	}
	public static int[] getNumberBlocks() {
		return numberBlocks;
	}
	public static void setNumberBlocks(int[] numberBlocks) {
		Config.numberBlocks = numberBlocks;
	}
	public static String getClusterJar() {
		return clusterJar;
	}
	public static void setClusterJar(String clusterJar) {
		Config.clusterJar = clusterJar;
	}
	public static String getLocalJar() {
		return localJar;
	}
	public static void setLocalJar(String localJar) {
		Config.localJar = localJar;
	}
	public static String getOriginalInputFilePath() {
		return originalInputFilePath;
	}
	public static void setOriginalInputFilePath(String localInput) {
		Config.originalInputFilePath = localInput;
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
		return session;
	}
	public static void setSession(Session session) {
		Config.session = session;
	}
	public static ArrayList<String> getScriptFiles() {
		return scriptFiles;
	}
	public static void setScriptFiles(ArrayList<String> pathScripts) {
		Config.scriptFiles = pathScripts;
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
	public static Boolean getConfigured() {
		return configured;
	}
	public static void setConfigured(Boolean configured) {
		Config.configured = configured;
	}
	public static void init(String host,int port,String pseudo,String pw) {
		Config.host = host;
		Config.port = port;
		Config.pseudo = pseudo;
		Config.pw = pw;
		Config.setConfigured(true);
		System.out.println("Got Config: " +Config.host );
	}
	public static void addScriptFile(String scriptFile) {
		if (scriptFiles == null) {
			scriptFiles = new ArrayList<String>();
		}
		scriptFiles.add(scriptFile);
	}
	
	public static String getClusterInput() {
		return clusterPath;
	}
	public static void openInput() {
		Img<FloatType> image = IOFunctions.openAs32Bit( new File( Config.getOriginalInputFilePath()) );
		setInputFile(image);	
	}
	public static Img<FloatType> getInputFile() {
		return inputFile;
	}
	public static void setInputFile(Img<FloatType> inputFile) {
		Config.inputFile = inputFile;
	}
	
    
	
}
