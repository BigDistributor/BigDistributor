package net.preibisch.distribution.io;

import org.apache.commons.io.FilenameUtils;

import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;

public enum DataExtension {
	N5,TIF, JAR,XML,MODEL;
	
	  public static DataExtension fromString(String string) {
		switch (string) {
		case "n5":
			return DataExtension.N5;
		case "tif":
			return DataExtension.TIF;
		case "xml":
			return DataExtension.XML;
		case "jar":
			return DataExtension.JAR;		
		case "model":
			return DataExtension.MODEL;
		default:
			MyLogger.log().error("Error extension: " + string);
			return null;
		}
	}
	 
	  public String file(String name) {
		  return String.format("%s.%s",name,toString());
	  }
	  
	  public String toString() {
			switch (this) {
			case N5:
				return "n5";
			case TIF:
				return "tif";
			case XML:
				return "xml";
			case JAR:
				return "jar";		
			case MODEL:
				return  "model";
			default:
				MyLogger.log().error("Error extension: " + this);
				return null;
			}
		}
	  
	  public static DataExtension fromURI(String path) {
		 String extension =  FilenameUtils.getExtension(path);
		  return DataExtension.fromString(extension);
	  }
	  
	  public static String removeExtension(String path) {
		  return FilenameUtils.removeExtension(path);
		  }
}