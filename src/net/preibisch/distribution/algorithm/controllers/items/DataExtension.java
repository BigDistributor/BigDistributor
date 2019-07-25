package net.preibisch.distribution.algorithm.controllers.items;

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
			System.out.println("Error extension: " + string);
			return null;
		}
	}
	  
	  public static DataExtension fromURI(String path) {
		  int j = path.lastIndexOf('.');
		  String extension = path.substring(j+1);
		  return DataExtension.fromString(extension);
	  }
}

