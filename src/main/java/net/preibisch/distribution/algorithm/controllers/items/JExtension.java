package main.java.net.preibisch.distribution.algorithm.controllers.items;

public enum JExtension {
	N5,TIF, JAR,XML,MODEL;
	
	  public static JExtension fromString(String string) {
		switch (string) {
		case "N5":
			return JExtension.N5;
		case "tif":

			return JExtension.TIF;
		case "xml":

			return JExtension.XML;
		case "jar":

			return JExtension.JAR;
			
		case "model":
			return JExtension.MODEL;

		default:
			System.out.println("Error extension: " + string);
			return null;
		}
	}
}

