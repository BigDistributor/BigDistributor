package clustering;
 
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SCP{
	
	String userName = "";
	String password = "";
	String host = "localhost";
	int port = 22;
	String path = "";
	
	public void send() {
		send(userName, password, host, port, path);
	}
	
	public void send(String userName, String password, String host, int port, String path) {

        Session session = null;
        Channel channel = null;
        try {
            JSch ssh = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            session = ssh.getSession(userName, host, port);
            session.setPassword(password);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            
            sftp.get(path, "/Users/Marwan/sendMe.jpg");
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

    }
    
}