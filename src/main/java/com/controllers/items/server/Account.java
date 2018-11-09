package main.java.com.controllers.items.server;

public class Account {
	private final String pseudo;
	private final String password;

	private Account(Builder builder) {
		this.pseudo = builder.pseudo;
		this.password = builder.password;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getPassword() {
		return password;
	}

	public static class Builder {
		private String pseudo = "mzouink";
		private String password = "";

		public Builder pseudo(final String pseudo) {
			this.password = pseudo;
			return this;
		}

		public Builder password(final String password) {
			this.password = password;
			return this;
		}

		public Account build() {
			return new Account(this);
		}

	}

}
