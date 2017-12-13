package master;

import java.util.ArrayList;

import Helpers.Helper;
import Helpers.Portion;
import ij.ImageJ;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class SplitImageIntoBlocs {

	public SplitImageIntoBlocs() throws ImgIOException {
		Helper.log = false;
		Img<FloatType> image = new ImgOpener().openImg("img/DrosophilaWing.tif", new FloatType());
		ImageJFunctions.show(image);
		
		// We will split the image in 4 columns 2 rows
		final int columns = 4;
		final int rows = 2;
		Helper.sigma = 0;
		FinalInterval interval = Helper.getFinalInterval(image);
		ArrayList<Portion> portions = Helper.splitImageEnBlocs(image,interval, columns, rows);
		saveImages(portions);
		for(Portion portion:portions)
			ImageJFunctions.show(portion.getView());

		//TODO save files
		//send them + get them back
	}

	
	private void saveImages(ArrayList<Portion> portions) {
		for (int k = 0; k < portions.size(); k++) {
			ImgSaver saver = new ImgSaver();
			String imgName = k + "-part.tif";
			try {
				ij.IJ.save(ImageJFunctions.wrap(portions.get(k).getView(), ""), imgName);
			}
			catch (Exception exc) {
				System.out.println("error "+k);
				exc.printStackTrace();
			}
		}
	}


	public static void main(String[] args) throws ImgIOException {
		new ImageJ();
		new SplitImageIntoBlocs();
	}
	
}
