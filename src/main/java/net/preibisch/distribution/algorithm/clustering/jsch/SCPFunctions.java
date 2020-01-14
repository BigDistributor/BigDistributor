package net.preibisch.distribution.algorithm.clustering.jsch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class SCPFunctions {

	public static void runCommand(String command) throws JSchException {
		SessionManager.validateConnection();
		System.out.println("$- " + command);
		Channel channel = SessionManager.getCurrentSession().openChannel("exec");
//		callBack.log(command);
		((ChannelExec) channel).setCommand(command);
		channel.connect();
	}

	public static void generateQStatLog(String logFile, AbstractCallBack callBack) throws JSchException {
		SessionManager.validateConnection();
		String command = "qstat > " + logFile;
		Channel channel = SessionManager.getCurrentSession().openChannel("exec");
		callBack.log(command);
		((ChannelExec) channel).setCommand(command);
		channel.connect();
	}


	public static void get(String remoteFile, String localFile)
			throws JSchException, IOException, FileNotFoundException {
		FileOutputStream fos = null;
		String prefix = null;
		if (new File(localFile).isDirectory()) {
			prefix = localFile + File.separator;
		}

		// exec 'scp -f rfile' remotely
		String command = "scp -f " + remoteFile;
		Channel channel = SessionManager.getCurrentSession().openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] buf = new byte[TCPProperties.BUFFER_SIZE];

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
				filesize = filesize * 10L + buf[0] - '0';
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
				throw new JSchException();
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
		}
	}

	public static void send(String localFile, String remoteFile)
			throws JSchException, IOException, FileNotFoundException {
		SessionManager.validateConnection();
		FileInputStream fis = null;
		boolean ptimestamp = true;

		// exec 'scp -t rfile' remotely
		String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteFile;
		Channel channel = SessionManager.getCurrentSession().openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		if (checkAck(in) != 0) {
			throw new JSchException();
		}

		File _lfile = new File(localFile);
		System.out.println("File: " + localFile + " |Size:" + _lfile.length());

		if (ptimestamp) {
			command = "T" + (_lfile.lastModified() / 1000) + " 0";
			// The access time should be sent here,
			// but it is not accessible with JavaAPI ;-<
			command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new JSchException();
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
			throw new JSchException();
		}

		// send a content of lfile
		fis = new FileInputStream(localFile);
		byte[] buf = new byte[TCPProperties.BUFFER_SIZE];
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
			throw new JSchException();
		}
		out.close();

		channel.disconnect();
	}
	
	public static void recursiveFolderUpload(ChannelSftp channelSftp,String sourcePath, String destinationPath)
			throws SftpException, FileNotFoundException {

		File sourceFile = new File(sourcePath);
		if (sourceFile.isFile()) {

			// copy if it is a file
			channelSftp.cd(destinationPath);
			if (!sourceFile.getName().startsWith("."))
				channelSftp.put(new FileInputStream(sourceFile), sourceFile.getName(), ChannelSftp.OVERWRITE);

		} else {

			System.out.println("inside else " + sourceFile.getName());
			File[] files = sourceFile.listFiles();

			if (files != null && !sourceFile.getName().startsWith(".")) {

				channelSftp.cd(destinationPath);
				SftpATTRS attrs = null;

				// check if the directory is already existing
				try {
					attrs = channelSftp.stat(destinationPath + "/" + sourceFile.getName());
				} catch (Exception e) {
					System.out.println(destinationPath + "/" + sourceFile.getName() + " not found");
				}

				// else create a directory
				if (attrs != null) {
					System.out.println("Directory exists IsDir=" + attrs.isDir());
				} else {
					System.out.println("Creating dir " + sourceFile.getName());
					channelSftp.mkdir(sourceFile.getName());
				}

				for (File f : files) {
					recursiveFolderUpload(channelSftp,f.getAbsolutePath(), destinationPath + "/" + sourceFile.getName());
				}

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

	public static void mkdir(String path, String folder) throws JSchException, SftpException {
		SessionManager.validateConnection();
		Channel channel = SessionManager.getCurrentSession().openChannel("sftp"); // Open SFTP Channel
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		channelSftp.cd(path);
		channelSftp.mkdir(folder);
//		String command = "mkdir " + path;
//		SCPFunctions.runCommand(command, new Callback());
	}
}