package slave;

import java.io.File;
import java.util.ArrayList;

public class GaussImage {
//TODO get image from folder 
	//Gauss filer + save
	public GaussImage() {
		// TODO Auto-generated constructor stub
		File[] files = getFiles();
	}

private File[] getFiles() {
	// TODO Auto-generated method stub
	File folder = new File("img");
	File[] listOfFiles = folder.listFiles();

	return listOfFiles;
//	    for (int i = 0; i < listOfFiles.length; i++) {
//	    	System.out.println(listOfFiles[i].getPath());
////	      if (listOfFiles[i].isFile()) {
////	        System.out.println("File " + listOfFiles[i].getName());
////	      } else if (listOfFiles[i].isDirectory()) {
////	        System.out.println("Directory " + listOfFiles[i].getName());
////	      }
//	    }
//	return null;
}
public static void main(String[] args) {
	new GaussImage();
}
}
