package main.java.com.tools;

enum Extension {
	TIF, JAR;
	
	  public static Extension formString(String string) {
		switch (string) {
		case "tif":

			return Extension.TIF;
		case "jar":

			return Extension.JAR;

		default:
			System.out.println("Error extension: " + string);
			return null;
		}
	}
}

