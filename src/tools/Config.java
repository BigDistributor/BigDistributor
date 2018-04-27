package tools;

import com.jcraft.jsch.Session;

public enum Config {
	INSTANCE;
	private static String pseudo = "mzouink";
	private static String pw = "";
	private static String host = "maxlogin2.mdc-berlin.net";
	private static int port = 22;
	private static String path = "";
	private static Boolean configured = false;
	private static String scriptFile = "";
	private static String clusterPath = "/scratch/AG_Preibisch/Marwan/clustering/";
	private static String[] localInputs;
	private static String[] clusterInputs;
	private static String localJar;
	private static String clusterJar;
	private static String localInput;
	private static String clusterInput;
	private static Session session;
	private static int sigma = 5; 
	
	
	public static String getClusterJar() {
		return clusterJar;
	}
	public static void setClusterJar(String clusterJar) {
		Config.clusterJar = clusterJar;
	}
	public static String getClusterInput() {
		return clusterInput;
	}
	public static void setClusterInput(String clusterInput) {
		Config.clusterInput = clusterInput;
	}
	public static String getLocalJar() {
		return localJar;
	}
	public static void setLocalJar(String localJar) {
		Config.localJar = localJar;
	}
	public static String getLocalInput() {
		return localInput;
	}
	public static void setLocalInput(String localInput) {
		Config.localInput = localInput;
	}
	public static int getSigma() {
		return sigma;
	}
	public static void setSigma(int sigma) {
		Config.sigma = sigma;
	}
	public static String getScriptFile() {
		return scriptFile;
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
	public static String getLocalInputString() {
		return String.join(" ", localInputs);
	}
	public static String getClusterInputString() {
		return String.join(" ", clusterInputs);
	}
	public static String[] getLocalInputs() {
		return localInputs;
	}
	public static void setLocalInputs(String[] localInputs) {
		Config.localInputs = localInputs;
	}
	public static String[] getClusterInputs() {
		return clusterInputs;
	}
	public static void setClusterInputs(String[] clusterInputs) {
		Config.clusterInputs = clusterInputs;
	}
	public static String getsSriptFile() {
		return scriptFile;
	}
	public static void setScriptFile(String pathScript) {
		Config.scriptFile = pathScript;
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
	
}
