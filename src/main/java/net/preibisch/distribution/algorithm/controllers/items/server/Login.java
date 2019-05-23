package main.java.net.preibisch.distribution.algorithm.controllers.items.server;

import java.util.UUID;

import com.jcraft.jsch.JSchException;

import main.java.net.preibisch.distribution.gui.LoginUI;
import main.java.net.preibisch.distribution.tools.config.DEFAULT;
import main.java.net.preibisch.distribution.tools.config.server.Account;
import main.java.net.preibisch.distribution.tools.config.server.ServerConfiguration;

public class Login {

	private static String id;
	private static Account account;
	private static ServerConfiguration server;

	public static void setAccount(Account account) {
		Login.account = account;
	}

	public static String getId() {
		return id;
	}

	public static Account getAccount() {
		return account;
	}

	public static ServerConfiguration getServer() {
		return server;
	}

	private static String id() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void login() {
		Login.id = id();
//		new LoginUI();
		Login.account = new Account(DEFAULT.USER_PSEUDO, null);
		Login.server = new ServerConfiguration.Builder().getDefault();

	}

	public static void login(ServerConfiguration server, Account account) {
		Login.id = id();
		Login.server = server;
		Login.account = account;
	}

}
