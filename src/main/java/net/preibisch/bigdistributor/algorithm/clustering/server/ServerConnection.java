package net.preibisch.bigdistributor.algorithm.clustering.server;

public abstract class ServerConnection {
	
	private static ServerConfiguration instance;
	
	public static ServerConfiguration get() throws IllegalAccessException {
		if (instance==null) {
			throw new IllegalAccessException("Init connection first !");
		}
		return instance;
	}
	
	public void connect() {
	}
	
	public void testConnection() {};

}
