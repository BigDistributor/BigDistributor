package Helpers;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.comparator.NameFileComparator;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class Helper {

	public enum Task {
		Gaus, OriginalSize, Collect;
	}

	public static Boolean log;
	public static int count, sigma;

	public static float computeMiLocation(final Img<FloatType> input, int startPoint, int arrivePoint) {
		final Cursor<FloatType> cursor = input.cursor();

		cursor.jumpFwd(startPoint);
		float min = cursor.next().getRealFloat();
		for (long j = 0; j < arrivePoint; ++j) {
			final float v = cursor.next().getRealFloat();

			min = Math.min(min, v);
		}
		return min;
	}

	public static RandomAccessible<FloatType> extend(Img<FloatType> image, int sigma2) {
		RandomAccessible<FloatType> infiniteImg = Views.extendMirrorSingle(image);

		long[] min = new long[image.numDimensions()];
		long[] max = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			min[d] = -Helper.sigma;
			max[d] = image.dimension(d) + Helper.sigma;
		}
		FinalInterval interval = new FinalInterval(min, max);
		return Views.interval(infiniteImg, interval);

	}

	public static long[] getDimensions(Img<FloatType> image) {
		long[] dimensions = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			dimensions[d] = image.dimension(d);
			System.out.println(d + " - " + image.dimension(d));
		}

		return dimensions;
	}

	public static ArrayList<Portion> getMainSize(ArrayList<Portion> portions) {
		ArrayList<Portion> newPortions = new ArrayList<Portion>();
		for (Portion portion : portions) {
			RandomAccessibleInterval<FloatType> view = Views.interval(portion.getView(),
					new long[] { Helper.sigma, Helper.sigma },
					new long[] { portion.getView().dimension(0) - Helper.sigma,
							portion.getView().dimension(0) - Helper.sigma });
			newPortions.add(new Portion(view, portion.getPosition(),portion.getMax(),portion.getSize()));
		}
		return newPortions;
	}

	public static RandomAccessibleInterval<FloatType> targetPositon(RandomAccessibleInterval<FloatType> targetView,
			Rectangle shape) {
		RandomAccessibleInterval<FloatType> view = Views.interval(targetView,
				new long[] { (long) shape.getX(), (long) shape.getY() },
				new long[] { (long) shape.getX() + (long) shape.getWidth(),
						(long) shape.getY() + (long) shape.getHeight() });

		return view;
	}

	public static FinalInterval getFinalInterval(RandomAccessibleInterval<FloatType> image) {
		long[] min = new long[image.numDimensions()];
		long[] max = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			min[d] = -Helper.sigma;
			max[d] = image.dimension(d) + Helper.sigma;
		}
		FinalInterval interval = new FinalInterval(min, max);
		// TODO Auto-generated method stub
		return interval;
	}

	public static void splitImage(RandomAccessibleInterval<FloatType> input, ArrayList<Portion> result, int dim,
			int slice) {
		if (input.numDimensions() > 2) {
			for (int j = 0; j < input.dimension(input.numDimensions() - 1); j++) {
				splitImage(Views.hyperSlice(input, input.numDimensions() - 1, j), result, input.numDimensions() - 1, j);
			}
		} else {
			result.add(new Portion(input, dim, slice));
		}
	}

	public static ArrayList<myTask> createThreadTasks(ArrayList<Portion> portions, Img<FloatType> resultImage,
			Task type) {

		ArrayList<myTask> taskList = new ArrayList<myTask>();
		switch (type) {
		case Gaus:
			for (Portion portion : portions) {
				myTask task = new myTask(portion, resultImage, Task.Gaus);
				taskList.add(task);
			}
			break;

		default:
			break;
		}
		return taskList;
	}

	public static void showImagesInFolder(String processFolder) {
		final File folder = new File(processFolder);
		  for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	showImagesInFolder(processFolder);
		        } else {
		            System.out.println(fileEntry.getPath());
	
		    		Img<FloatType> image;
					try {
						image = new ImgOpener().openImg(fileEntry.getPath(), new FloatType());
						ImageJFunctions.show(image);
					} catch (ImgIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		        }
		    }
		
	}
	
	public static ArrayList<Img<FloatType>> getImagesFromFolder(String processFolder) {
		ArrayList<Img<FloatType>> images = new ArrayList<Img<FloatType>>();
		final File folder = new File(processFolder);
File[] files = folder.listFiles();
Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);
for (final File fileEntry : files) {
	  System.out.println(fileEntry.getPath());};
		  for (final File fileEntry : files) {
			  System.out.println(fileEntry.getPath());
		        if (fileEntry.isDirectory()) {
		        	
//		        	showImagesInFolder(processFolder);
		        } else {
//		            System.out.println(fileEntry.getPath());
	
		    		
					try {
						Img<FloatType> image = new ImgOpener().openImg(fileEntry.getPath(), new FloatType());
						images.add(image);
//						ImageJFunctions.show(image);
					} catch (ImgIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		        }
		    }
		return images;
	}
	
	
	public static long[] minus(long[] array, int x) {
		long[] result= new long[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i]= array[i]-x;
		}
		return result;
	}

	public static long[] add(long[] array, int x) {
		long[] result= new long[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i]= array[i]+x;
		}
		return result;
	}


}
