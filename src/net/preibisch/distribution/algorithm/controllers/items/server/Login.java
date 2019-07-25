package net.preibisch.distribution.algorithm.controllers.items.server;

import net.preibisch.distribution.tools.config.DEFAULT;
import net.preibisch.distribution.tools.config.server.Account;
import net.preibisch.distribution.tools.config.server.ServerConfiguration;

public class Login {

	private static Account account;
	private static ServerConfiguration server;
	private static boolean configured = false;

	public static void setAccount(Account account) {
		Login.account = account;
	}

	
	public static Account getAccount() {
		return account;
	}

	public static ServerConfiguration getServer() {
		return server;
	}

	

	public static void login() {
//		new LoginUI();
		Login.configured = true;
		Login.account = new Account(DEFAULT.USER_PSEUDO, null);
		Login.server = new ServerConfiguration.Builder().getDefault();

	}

	public static void login(ServerConfiguration server, Account account) {	
		Login.server = server;
		Login.account = account;
	}
	public static boolean isConfigured() {
		return configured;
	}

}
