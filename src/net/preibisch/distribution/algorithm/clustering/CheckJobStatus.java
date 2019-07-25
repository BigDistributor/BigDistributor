package net.preibisch.distribution.algorithm.clustering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CheckJobStatus {
	   int iExitValue;
	    String sCommandString;

	    public void runScript(String command){
	    	  try {
	              Process proc = Runtime.getRuntime().exec(command);
	              BufferedReader read = new BufferedReader(new InputStreamReader(
	                      proc.getInputStream()));
	              try {
	                  proc.waitFor();
	              } catch (InterruptedException e) {
	                  System.out.println(e.getMessage());
	              }
	              while (read.ready()) {
	                  System.out.println("hello: "+read.readLine());
	              }
	          } catch (IOException e) {
	              System.out.println(e.getMessage());
	          }
	    }
	    
	    
	    public static int countRunningJobs(){
	    	int lines = 0;
	    	  try {
	              Process proc = Runtime.getRuntime().exec("qstat");
	              BufferedReader read = new BufferedReader(new InputStreamReader(
	                      proc.getInputStream()));
	              try {
	                  proc.waitFor();
	              } catch (InterruptedException e) {
	                  System.out.println(e.getMessage());
	              }
	              while (read.readLine() != null) lines++;
	              read.close();
	          } catch (IOException e) {
	              System.out.println(e.getMessage());
	          }
			return lines;
	    }

	    public static void main(String args[]){
	    	System.out.println("jobs: "+countRunningJobs());
	    }
}
