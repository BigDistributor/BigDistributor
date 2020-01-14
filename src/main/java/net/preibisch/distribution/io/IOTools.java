package net.preibisch.distribution.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import ij.ImagePlus;
import ij.VirtualStack;
import ij.io.FileInfo;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.io.TiffEncoder;
import ij.process.ImageProcessor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.ImgLibException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImg;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class IOTools {

	public static <T extends RealType<T> & NativeType<T>> ImagePlus getImagePlusInstance(
			final RandomAccessibleInterval<T> img) {
		return getImagePlusInstance(img, true, "img", Double.NaN, Double.NaN);
	}

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> ImagePlus getImagePlusInstance(
			final RandomAccessibleInterval<T> img, final boolean virtualDisplay, final String title, final double min,
			final double max) {
		ImagePlus imp = null;

		if (img instanceof ImagePlusImg)
			try {
				imp = ((ImagePlusImg<T, ?>) img).getImagePlus();
			} catch (ImgLibException e) {
				System.out.println(e.toString());
			}

		if (imp == null) {
			if (virtualDisplay)
				imp = ImageJFunctions.wrap(img, title);
			else
				imp = ImageJFunctions.wrap(img, title).duplicate();
		}

		final double[] minmax = getFusionMinMax(img, min, max);

		imp.setTitle(title);
		imp.setDimensions(1, (int) img.dimension(2), 1);
		imp.setDisplayRange(minmax[0], minmax[1]);

		return imp;
	}

	public static <T extends RealType<T>> double[] getFusionMinMax(final RandomAccessibleInterval<T> img,
			final double min, final double max) {
		final double[] minmax;

		if (Double.isNaN(min) || Double.isNaN(max))
			minmax = minMaxApprox(img);
		else if (min == 0 && max == 65535) {
			// 16 bit input was assumed, little hack in case it was 8-bit
			minmax = minMaxApprox(img);
			if (minmax[1] <= 255) {
				minmax[0] = 0;
				minmax[1] = 255;
			}
		} else
			minmax = new double[] { (float) min, (float) max };

		return minmax;
	}

	public static <T extends RealType<T>> double[] minMaxApprox(final RandomAccessibleInterval<T> img) {
		return minMaxApprox(img, 1000);
	}

	public static <T extends RealType<T>> double[] minMaxApprox(final RandomAccessibleInterval<T> img,
			final int numPixels) {
		return minMaxApprox(img, new Random(3535), numPixels);
	}

	public static <T extends RealType<T>> double[] minMaxApprox(final RandomAccessibleInterval<T> img, final Random rnd,
			final int numPixels) {
		final RandomAccess<T> ra = img.randomAccess();

		// run threads and combine results
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;

		for (int i = 0; i < numPixels; ++i) {
			for (int d = 0; d < img.numDimensions(); ++d)
				ra.setPosition(rnd.nextInt((int) img.dimension(d)) + (int) img.min(d), d);

			final double v = ra.get().getRealDouble();

			min = Math.min(min, v);
			max = Math.max(max, v);
		}

		return new double[] { min, max };
	}

	/*
	 * Reimplementation from ImageJ FileSaver class. Necessary since it traverses
	 * the entire virtual stack once to collect some slice labels, which takes
	 * forever in this case.
	 */
	public static boolean saveTiffStack(final ImagePlus imp, final String path, AbstractCallBack callback) {
		FileInfo fi = imp.getFileInfo();
		boolean virtualStack = imp.getStack().isVirtual();
		if (virtualStack)
			fi.virtualStack = (VirtualStack) imp.getStack();
		fi.info = imp.getInfoProperty();
		fi.description = new FileSaver(imp).getDescriptionString();
		DataOutputStream out = null;
		try {
			TiffEncoder file = new TiffEncoder(fi);
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			file.write(out);
			out.close();
		} catch (IOException e) {
			callback.log(new Date(System.currentTimeMillis()) + ": ERROR: Cannot save file '" + path + "':" + e);
			return false;
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				}
		}
		return true;
	}

	public static Img<FloatType> openAs32Bit(final File file) {
		return openAs32Bit(file, new ArrayImgFactory<FloatType>());
	}

	@SuppressWarnings("unchecked")
	public static ArrayImg<FloatType, ?> openAs32BitArrayImg(final File file) {
		return (ArrayImg<FloatType, ?>) openAs32Bit(file, new ArrayImgFactory<FloatType>());
	}

	public static Img<FloatType> openAs32Bit(final File file, final ImgFactory<FloatType> factory) {
		if (!file.exists())
			throw new RuntimeException("File '" + file.getAbsolutePath() + "' does not exisit.");

		final ImagePlus imp = new Opener().openImage(file.getAbsolutePath());

		if (imp == null)
			throw new RuntimeException("File '" + file.getAbsolutePath() + "' coult not be opened.");

		final Img<FloatType> img;

		if (imp.getStack().getSize() == 1) {
			// 2d
			img = factory.create(new int[] { imp.getWidth(), imp.getHeight() }, new FloatType());
			final ImageProcessor ip = imp.getProcessor();

			final Cursor<FloatType> c = img.localizingCursor();

			while (c.hasNext()) {
				c.fwd();

				final int x = c.getIntPosition(0);
				final int y = c.getIntPosition(1);

				c.get().set(ip.getf(x, y));
			}

		} else {
			// >2d
			img = factory.create(new int[] { imp.getWidth(), imp.getHeight(), imp.getStack().getSize() },
					new FloatType());

			final Cursor<FloatType> c = img.localizingCursor();

			// for efficiency reasons
			final ArrayList<ImageProcessor> ips = new ArrayList<ImageProcessor>();

			for (int z = 0; z < imp.getStack().getSize(); ++z)
				ips.add(imp.getStack().getProcessor(z + 1));

			while (c.hasNext()) {
				c.fwd();

				final int x = c.getIntPosition(0);
				final int y = c.getIntPosition(1);
				final int z = c.getIntPosition(2);

				c.get().set(ips.get(z).getf(x, y));
			}
		}

		imp.close();

		return img;
	}

	public static void cleanFolder(String folderName) {
		File file = new File(folderName);
		file.mkdir();
		try {
			FileUtils.cleanDirectory(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
