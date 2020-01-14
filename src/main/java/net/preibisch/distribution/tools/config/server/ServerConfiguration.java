package net.preibisch.distribution.tools.config.server;

import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.tools.config.DEFAULT;

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
		
		private String host;
		private int port;
		private String path;

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
		@Override
		public String toString() {
			return "Host: "+host+" |Port:"+port+" | ClusterPath:"+path;
		}
		
		public ServerConfiguration getDefault() {
			this.host = DEFAULT.HOST;
			this.path = DEFAULT.CLUSTER_PATH;
			this.port = DEFAULT.PORT;
			MyLogger.log.info(this.toString());
			return new ServerConfiguration(this);
		}
		
		public ServerConfiguration build() {
			MyLogger.log.info(this.toString());
			return new ServerConfiguration(this);
		}

	}
}
