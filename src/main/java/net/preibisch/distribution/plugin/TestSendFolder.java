package main.java.net.preibisch.distribution.plugin;

import java.io.FileNotFoundException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.clustering.jsch.SCPManager;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;

public class TestSendFolder {
public static void main(String[] args) throws FileNotFoundException, SftpException, JSchException {


	MyLogger.initLogger();
	Login.login();
	String localPath = "/home/mzouink/Desktop/testn5/";
	String clusterfoler = "/fast/AG_Preibisch/Marwan/clustering/test/";
	
	SCPManager.sendFolder(localPath, clusterfoler);
	
}
}
