package main.java.com.controllers.items;

public enum JExtension {
	TIF, JAR,XML,MODEL;
	
	  public static JExtension fromString(String string) {
		switch (string) {
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

