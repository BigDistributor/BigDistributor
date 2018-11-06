package main.java.com.clustering.jsch;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import main.java.com.clustering.MyCallBack;
import main.java.com.gui.items.Colors;
import main.java.com.tools.Config;
import main.java.com.tools.server.Login;

public class SCP {

	public static void connect(Login login) throws JSchException {
		JSch jsch = new JSch();
		Config.setSession(jsch.getSession(login.getAccount().getPseudo(), login.getServer().getHost(),login.getServer().getPort()));

		// username and password will be given via UserInfo interface.
		UserInfo ui = new MyUserInfo();
		Config.getSession().setUserInfo(ui);
		Config.getSession().connect();
	}

	public static void disconnect() {
		Config.getSession().disconnect();
	}

	public static void validateConnection(Login login) throws JSchException {
		if (Config.getSession() == null) {
			connect(login);
		}
		if (!Config.getSession().isConnected()) {
			connect(login);
		}
	}

	public static void run(Login login, String scriptFile,
			MyCallBack callBack) throws JSchException {
		validateConnection(login);
		String command = "cd " + Config.getLogin().getServer().getPath() + " && chmod +x " + scriptFile + " && ./" + scriptFile;
		System.out.println(command);
		Channel channel = Config.getSession().openChannel("exec");
		callBack.log(command);
		((ChannelExec) channel).setCommand(command);
		channel.connect();
	}

	public static void generateLog(Login login, String scriptPath, MyCallBack callBack)
			throws JSchException {
		validateConnection(login);
		String command = "cd " + scriptPath + " && qstat > log.txt";
		Channel channel = Config.getSession().openChannel("exec");
		callBack.log(command);
		((ChannelExec) channel).setCommand(command);
		channel.connect();
	}

	public static void get(Login login, String remoteFile, String localFile, int id)
			throws IOException, JSchException {
		FileOutputStream fos = null;
		validateConnection(login);
		String prefix = null;
		if (new File(localFile).isDirectory()) {
			prefix = localFile + File.separator;
		}

		// exec 'scp -f rfile' remotely
		String command = "scp -f " + remoteFile;
		Channel channel = Config.getSession().openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] buf = new byte[Config.BUFFER_SIZE];

		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();

		while (true) {
			int c = checkAck(in);
			if (c != 'C') {
				break;
			}

			// read '0644 '
			in.read(buf, 0, 5);

			long filesize = 0L;
			while (true) {
				if (in.read(buf, 0, 1) < 0) {
					// error
					break;
				}
				if (buf[0] == ' ')
					break;
				filesize = filesize * 10L + (long) (buf[0] - '0');
			}

			String file = null;
			for (int i = 0;; i++) {
				in.read(buf, i, 1);
				if (buf[i] == (byte) 0x0a) {
					file = new String(buf, 0, i);
					break;
				}
			}

			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			// read a content of lfile
			fos = new FileOutputStream(prefix == null ? localFile : prefix + file);
			int foo;
			while (true) {
				if (buf.length < filesize)
					foo = buf.length;
				else
					foo = (int) filesize;
				foo = in.read(buf, 0, foo);
				if (foo < 0) {
					// error
					break;
				}
				fos.write(buf, 0, foo);
				filesize -= foo;
				if (filesize == 0L)
					break;
			}
			fos.close();
			fos = null;

			if (checkAck(in) != 0) {
				System.exit(0);
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
		}

		// System.exit(0);
		if (id >= 0) {
			Config.blocksView.get(id).setStatus(Colors.GOT);
		}
	}

	public static void send(Login login,  String localFile, String remoteFile, int id)
			throws JSchException, IOException {
		FileInputStream fis = null;
		validateConnection(login);
		boolean ptimestamp = true;

		// exec 'scp -t rfile' remotely
		String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteFile;
		Channel channel = Config.getSession().openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		if (checkAck(in) != 0) {
			System.exit(0);
		}

		File _lfile = new File(localFile);

		if (ptimestamp) {
			command = "T " + (_lfile.lastModified() / 1000) + " 0";
			// The access time should be sent here,
			// but it is not accessible with JavaAPI ;-<
			command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
		}

		// send "C0644 filesize filename", where filename should not include '/'
		long filesize = _lfile.length();
		command = "C0644 " + filesize + " ";
		if (localFile.lastIndexOf('/') > 0) {
			command += localFile.substring(localFile.lastIndexOf('/') + 1);
		} else {
			command += localFile;
		}
		command += "\n";
		out.write(command.getBytes());
		out.flush();
		if (checkAck(in) != 0) {
			System.exit(0);
		}

		// send a content of lfile
		fis = new FileInputStream(localFile);
		byte[] buf = new byte[Config.BUFFER_SIZE];
		while (true) {
			int len = fis.read(buf, 0, buf.length);
			if (len <= 0)
				break;
			out.write(buf, 0, len); // out.flush();
		}
		fis.close();
		fis = null;
		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();
		if (checkAck(in) != 0) {
			System.exit(0);
		}
		out.close();

		channel.disconnect();

		// System.exit(0);
		if (id != -1) {
			try {
				Config.blocksView.get(id).setStatus(Colors.SENT);
				throw new Exception("Out of boxes");
			} catch (Exception e) {
				// Helper.log("Out of size");
			}
		}
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			Object[] options = { "yes", "no" };
			int foo = JOptionPane.showOptionDialog(null, str, "Warning", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			return foo == 0;
		}

		String passwd;
		JTextField passwordField = (JTextField) new JPasswordField(20);

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			Object[] ob = { passwordField };
			int result = JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				passwd = passwordField.getText();
				return true;
			} else {
				return false;
			}
		}

		public void showMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		private Container panel;

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx = 0;
			panel.add(new JLabel(instruction), gbc);
			gbc.gridy++;

			gbc.gridwidth = GridBagConstraints.RELATIVE;

			JTextField[] texts = new JTextField[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridx = 0;
				gbc.weightx = 1;
				panel.add(new JLabel(prompt[i]), gbc);

				gbc.gridx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weighty = 1;
				if (echo[i]) {
					texts[i] = new JTextField(20);
				} else {
					texts[i] = new JPasswordField(20);
				}
				panel.add(texts[i], gbc);
				gbc.gridy++;
			}

			if (JOptionPane.showConfirmDialog(null, panel, destination + ": " + name, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
				String[] response = new String[prompt.length];
				for (int i = 0; i < prompt.length; i++) {
					response[i] = texts[i].getText();
				}
				return response;
			} else {
				return null; // cancel
			}
		}
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				// Helper.log(sb.toString());
			}
			if (b == 2) { // fatal error
				// Helper.log(sb.toString());
			}
		}
		return b;
	}

	String passwd;
	JTextField passwordField = (JTextField) new JPasswordField(20);

	public String getPassphrase() {
		return null;
	}

	public boolean promptPassphrase(String message) {
		return true;
	}

	public boolean promptPassword(String message) {
		Object[] ob = { passwordField };
		int result = JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			passwd = passwordField.getText();
			return true;
		} else {
			return false;
		}
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
	private Container panel;

	public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
			boolean[] echo) {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		panel.add(new JLabel(instruction), gbc);
		gbc.gridy++;

		gbc.gridwidth = GridBagConstraints.RELATIVE;

		JTextField[] texts = new JTextField[prompt.length];
		for (int i = 0; i < prompt.length; i++) {
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.weightx = 1;
			panel.add(new JLabel(prompt[i]), gbc);

			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 1;
			if (echo[i]) {
				texts[i] = new JTextField(20);
			} else {
				texts[i] = new JPasswordField(20);
			}
			panel.add(texts[i], gbc);
			gbc.gridy++;
		}

		if (JOptionPane.showConfirmDialog(null, panel, destination + ": " + name, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			String[] response = new String[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				response[i] = texts[i].getText();
			}
			return response;
		} else {
			return null; // cancel
		}
	}
}