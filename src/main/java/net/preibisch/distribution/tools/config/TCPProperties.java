package main.java.net.preibisch.distribution.tools.config;

public class TCPProperties {
	public static int BUFFER_SIZE = 1024 * 1024;

	public static void setBufferSize(int size) {
		TCPProperties.BUFFER_SIZE = size ;
		
	}
}
