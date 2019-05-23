package main.java.net.preibisch.distribution.plugin;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SessionManager;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.io.img.XMLFile;
import mpicbg.spim.data.SpimDataException;

public class HeadlessApp {
	private final static String tmpDir = "/home/mzouink/Desktop/testsave/";
	private final static String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
	private final static String output_path = "/home/mzouink/Desktop/testn5/output45.n5";
	
	public static void main(String[] args) throws SpimDataException, IOException, JSchException {

		new ImageJ();
		MyLogger.initLogger();

		// Input XML
		XMLFile InputFile = new XMLFile(input_path);
		
		// Connection
		Login.login();
		SessionManager.connect();
		
		// Generate Metadata
		
		// Generate script
		
		// Generate batch
		
		// Generate output in server
		
		// Run
	
		
	}
}
