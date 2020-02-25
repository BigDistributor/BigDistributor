package net.preibisch.distribution.algorithm.clustering.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import net.preibisch.distribution.algorithm.clustering.server.Connection;
import net.preibisch.distribution.gui.PasswordPopup;

public class SessionManager {
	private static Session currentSession;
	private static int iteration = 0;

	public static Session getCurrentSession() {
		return currentSession;
	}

	public static void connect() throws JSchException {
		System.out.println("Connecting..");
		if (currentSession == null)
			openSession();

		try {
			if (currentSession.getUserInfo() == null)
				getPW();
			currentSession.connect();
		} catch (JSchException e) {
			if (iteration < 5) {
				openSession();
				getPW();
				connect();
				return;
			} else
				throw new JSchException("Error login ! ");
		}
		System.out.println("Connected to server");
	}

	public static void openSession() throws JSchException {
		System.out.println("opening Session..");
		JSch jsch = new JSch();
		currentSession = jsch.getSession(Connection.getAccount().getPseudo(), Connection.getServer().getHost(),
				Connection.getServer().getPort());
		System.out.println("Connection Session Opened !");
	}

	public static void disconnect() {
		currentSession.disconnect();
	}

	public static void validateConnection() throws JSchException {
		if (currentSession == null) {
			System.out.println("Open session");
			connect();
		}
		if (!currentSession.isConnected()) {
			System.out.println("Not connected");
			connect();
		}
	}

	private static void getPW() {
		// username and password will be given via UserInfo interface.
		UserInfo ui = new PasswordPopup();
		currentSession.setUserInfo(ui);
		iteration++;
	}
}
