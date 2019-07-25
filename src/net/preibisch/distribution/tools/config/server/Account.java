package net.preibisch.distribution.tools.config.server;

public class Account {
	private String pseudo;
	private char[] password;

	public Account(String pseudo, char[] password) {
		this.pseudo = pseudo;
		this.password = password;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getPassword() {
		return String.valueOf(password);
	}

}
