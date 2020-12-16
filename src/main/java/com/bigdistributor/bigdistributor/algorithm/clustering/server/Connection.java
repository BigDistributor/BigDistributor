package net.preibisch.bigdistributor.algorithm.clustering.server;

import net.preibisch.bigdistributor.tools.config.DEFAULT;

public class Connection  {

	private static Account account;
	private static ServerConfiguration server;
	private static boolean configured = false;

	public static void setAccount(Account account) {
		Connection.account = account;
	}
	
	public static Account getAccount() {
		return account;
	}

	public static ServerConfiguration getServer() {
		return server;
	}

	public static void login() {
//		new LoginUI();
		Connection.configured = true;
		Connection.account = new Account(DEFAULT.USER_PSEUDO, null);
		Connection.server = new ServerConfiguration.Builder().getDefault();
	}

	public static void login(ServerConfiguration server, Account account) {	
		Connection.server = server;
		Connection.account = account;
	}
	public static boolean isConfigured() {
		return configured;
	}

}
