package main.java.com.tools.server;

public class ServerConfiguration {

	private final String host;
	private final int port;
	private final String path;

	private ServerConfiguration(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.path = builder.path;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public String getPath() {
		return path;
	}

	public static class Builder {
		private String host = "maxlogin2.mdc-berlin.net";
		private int port = 22;
		private String path = "/fast/AG_Preibisch/Marwan/clustering/";

		public Builder host(final String host) {
			this.host = host;
			return this;
		}

		public Builder port(final int port) {
			this.port = port;
			return this;
		}
		
		public Builder path(final String path) {
			this.path = path;
			return this;
		}
		
		public ServerConfiguration build() {
			return new ServerConfiguration(this);
		}

	}
}
