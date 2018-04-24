package tools;

public enum Config {
	INSTANCE;
	private static String pseudo = "";
	private static String pw = "";
	private static String host = "localhost";
	private static int port = 22;
	private static String path = "";
	private static Boolean configured = false;
	private static String pathScript = "";
	private static String[] localInputs;
	private static String[] clusterInputs;

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
	public static String getPathScript() {
		return pathScript;
	}
	public static void setPathScript(String pathScript) {
		Config.pathScript = pathScript;
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
