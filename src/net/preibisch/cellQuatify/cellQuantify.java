package net.preibisch.cellQuatify;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class cellQuantify {

	public cellQuantify() throws ImgIOException {
		String string = "img/file.tif";
		Img<FloatType> image = new ImgOpener().openImg(string, new FloatType());
		ImageJFunctions.show(image).setTitle("Original Image");
//		image2 = threshold(image, 20);
		System.out.println("Dimensions: "+image.numDimensions());
	}
	
	public static void main(String[] args) throws ImgIOException {
		new cellQuantify();
	}
	
	public <T extends Comparable< T > & Type<T> > Img< BitType > threshold( IterableInterval< T > image, T threshold )
	{
		// create a new ImgLib2 image of same dimensions
		// but using BitType, which only requires 1 bit per pixel
		ImgFactory< BitType > imgFactory = new CellImgFactory< BitType >( 16 );
		Img< BitType > thresholdImg = imgFactory.create( image, new BitType() );

		// test if they have the same iteration order (here we know they don't)
		boolean sameIterationOrder = image.iterationOrder().equals( thresholdImg.iterationOrder() );
		System.out.println( "same iteration order = " + sameIterationOrder );
		
		if ( sameIterationOrder )
		{
			// create a cursor on the Img and the destination, it will iterate all pixels
			Cursor<T> cursor = image.cursor();
			Cursor<BitType> cursorThresholdImg = thresholdImg.cursor();
			
			// iterate over all pixels
			while ( cursor.hasNext() )
			{
				// get the value of the next pixel in the input
				T pixelValue = cursor.next();
	
				// get the value of the next pixel in the output
				BitType thresholdImgValue = cursorThresholdImg.next();
	
				// set the 0 or 1 depending on the value
				if ( pixelValue.compareTo( threshold ) > 0 )
					thresholdImgValue.setReal( 1 );
				else
					thresholdImgValue.setReal( 0 );			
			}
		}
		else
		{
			// create a localizing cursor on the Img, it will iterate all pixels
			Cursor<T> cursor = image.localizingCursor();

			// create a randomaccess on the binary image and translate it by the min of the input
			final long[] min = new long[ image.numDimensions() ];
			image.min( min );
			RandomAccess<BitType> randomAccess = Views.translate( thresholdImg, min ).randomAccess();
			
			// iterate over all pixels
			while ( cursor.hasNext() )
			{
				// get the value of the next pixel in the input
				T pixelValue = cursor.next();
				
				// set the random access to the location of the cursor
				randomAccess.setPosition( cursor );
				
				// get the value of the next pixel in the output
				BitType thresholdImgValue = randomAccess.get();
	
				// set the 0 or 1 depending on the value
				if ( pixelValue.compareTo( threshold ) > 0 )
					thresholdImgValue.setReal( 1 );
				else
					thresholdImgValue.setReal( 0 );			
			}			
		}
		
		return thresholdImg;
	}
}
