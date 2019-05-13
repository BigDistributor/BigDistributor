package main.java.net.preibisch.distribution.tools.config.server;

import main.java.net.preibisch.distribution.tools.config.DEFAULT;

public class Account {
	private final String pseudo;
	private final char[] password;

	private Account(Builder builder) {
		this.pseudo = builder.pseudo;
		this.password = builder.password;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getPassword() {
		return String.valueOf(password);
	}

	public static class Builder {
		private String pseudo ;
		private char[] password ;

		public Builder pseudo(final String pseudo) {
			this.pseudo = pseudo;
			return this;
		}

		public Builder password(final char[] password) {
			this.password = password;
			return this;
		}
		
		public Account getDefault() {
			this.pseudo = DEFAULT.USER_PSEUDO;
			this.password = DEFAULT.USER_PASSWORD;
			return new Account(this);
		}
		public Account build() {
			return new Account(this);
		}

	}

}
