package net.preibisch.bigdistributor.tools.helpers;


import ij.CompositeImage;
import ij.ImagePlus;
import ij.VirtualStack;
import ij.io.FileInfo;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.io.TiffEncoder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImpHelpers {

    private static final int FLOAT_TYPE = 2;

    public static void copy(final RandomAccessibleInterval<FloatType> source,
                            final RandomAccessibleInterval<FloatType> target) {
        final RandomAccess<FloatType> in = source.randomAccess();
        IterableInterval<FloatType> iterableTarget = Views.iterable(target);
        final Cursor<FloatType> out = iterableTarget.cursor();
        while (out.hasNext()) {
            out.fwd();
            in.setPosition(out);
            out.get().set(in.get());
        }
    }

    public static RandomAccessibleInterval<FloatType> concat(final RandomAccessibleInterval<FloatType> source1,
                                                             final RandomAccessibleInterval<FloatType> source2) {
        final FloatType type = new FloatType();
        final ArrayImgFactory<FloatType> factory = new ArrayImgFactory<FloatType>();
        final int n1 = getNChannels(source1);
        final int n2 = getNChannels(source2);
        final long[] dim = new long[3];
        for (int d = 0; d < 2; ++d)
            dim[d] = source1.dimension(d);
        dim[2] = n1 + n2;
        final RandomAccessibleInterval<FloatType> all = factory.create(dim, type);
        if (n1 == 1)
            copy(source1, Views.hyperSlice(all, 2, 0));
        else
            for (int i = 0; i < n1; i++) {
                copy(Views.hyperSlice(source1, 2, i), Views.hyperSlice(all, 2, i));
            }
        if (n2 == 1)
            copy(source2, Views.hyperSlice(all, 2, n1));
        else
            for (int i = 0; i < n2; i++) {
                copy(Views.hyperSlice(source2, 2, i), Views.hyperSlice(all, 2, n1 + i));
            }
        return all;
    }

    public static int getNChannels(RandomAccessibleInterval<FloatType> source) {
        if (source.numDimensions() == 2)
            return 1;
        else
            return (int) (source.dimension(2));
    }

    public static <T> long[] getDims(RandomAccessibleInterval<T> img) {
        long dims[] = new long[img.numDimensions()];
        for (int i = 0; i < img.numDimensions(); i++) {
            dims[i] = img.dimension(i);
        }
        return dims;
    }

    public static String toString(long[] dims) {
        String result = "(";
        for (int i = 0; i < dims.length; i++) {
            result += String.valueOf(dims[i]);
            if (i < dims.length - 1) {
                result += "-";
            }
        }
        result += ")";
        return result;
    }

    public static <T> void printDims(RandomAccessibleInterval<T> img) {
        long[] dims = getDims(img);
        String string = toString(dims);
        Log.info("Dims: " + img.numDimensions() + "-" + string);

    }

    public static ImagePlus concat(ImagePlus imp1, ImagePlus imp2) {
        Img<FloatType> img1 = ImageJFunctions.wrap(imp1);
        Img<FloatType> img2 = ImageJFunctions.wrap(imp2);
        RandomAccessibleInterval<FloatType> imgAll = concat(img1, img2);
        return ImageJFunctions.wrap(imgAll, "");
    }

//    public static void printDims(ImagePlus imp) {
//        Img<FloatType> img = ImageJFunctions.wrap(imp);
//        printDims(img);
//    }

    public static void printInfos(ImagePlus imp) {
        Log.info(imp.getFileInfo().toString());
        Log.info("Stack: " + imp.getStack().size());
        Log.info("Channel: " + imp.getNChannels());
        Log.info("Dims: "+Utils.toString(imp.getDimensions()));
        Log.info("Type: " + imp.getType());
    }

//    public static <T extends Type<T>> ImagePlus getComposite(ImagePlus original) {
//        return getComposite(original, 0);
//    }

    // IMP Convert to float
    // new ImagePlus("float" ,impMask.getProcessor().convertToFloatProcessor() );

    //Code from Stephan
//        for ( int i = 0; i < imp.getStackSize(); ++i )
//        {  Color.GREEN
//            imp.getStack().getProcessor( i + 1 ).setColor(Colors.getColor(ColorUtilsKt.randomColor(),Color.BLUE));
//            imp.getStack().getProcessor( i + 1 ).setMinAndMax( 10, 100 );
//        }
//        impMask.setDisplayRange(0,300);

    public static CompositeImage getComposite(ImagePlus imp, int extras) {
        Img<FloatType> output = createBlackOutput(imp);
        for (int i = 0; i < extras; i++)
            imp.getStack().addSlice(ImageJFunctions.wrap(output.copy(), String.valueOf(i)).getProcessor());

        imp.setDimensions(imp.getStackSize(), 1, 1);

        CompositeImage comp = new CompositeImage(imp, CompositeImage.COMPOSITE);

        return comp;
    }

//    public static ImagePlus(conca)

    public static Img<FloatType> createBlackOutput(ImagePlus imp) {
        int[] dims = imp.getDimensions();
        return createBlackImg(dims[0],dims[1]);
    }
    
    public static Img<FloatType> createBlackImg(long dim1, long dim2) {
        final ImgFactory<FloatType> imgFactory = new CellImgFactory<>(new FloatType(), 5);
        final Img<FloatType> img = imgFactory.create(dim1, dim2);
        return img;
    }
    
    public static Img<FloatType> createBlackOutput(RandomAccessibleInterval<FloatType> img) {
        long[] dims = ImgHelpers.getDimensions(img);
        return createBlackImg(dims[0],dims[1]);
    }

    public static Image toImage(BufferedImage buff) {
        Image fxImage = SwingFXUtils.toFXImage(buff, null);
        return fxImage;
    }

    public static float getValue(Img<FloatType> img, int x, int y) {
        RandomAccess<FloatType> cursor = img.randomAccess();
        cursor.setPosition(x, 0);
        cursor.setPosition(y, 1);
        FloatType val = cursor.get();
        return val.get();
    }

    public static int getValue(Img<UnsignedByteType> img, int x, int y, int channel) {
        RandomAccess<UnsignedByteType> cursor = img.randomAccess();
        cursor.setPosition(x, 0);
        cursor.setPosition(y, 1);
        cursor.setPosition(channel, 2);
        UnsignedByteType val = cursor.get();
        return val.getInteger();
    }

    public static long add(Img<FloatType> masks, IntervalView<FloatType> result, float value, int category) {
        long size = 0 ;
        Cursor<FloatType> cursorInput = masks.cursor();

        RandomAccess<FloatType> randomAccess = result.randomAccess();

        while (cursorInput.hasNext()) {
            cursorInput.fwd();
            if (cursorInput.get().get() == value) {
                randomAccess.setPosition(cursorInput);
                randomAccess.get().set(category);
                size += 1;
            }
        }
        Log.info("size: "+size);
        return size;
    }

    public static void setValueWithoutCenter(Img<FloatType> img, Img<FloatType> result, float value, int setValue) {
        Cursor<FloatType> cursorInput = img.cursor();
        RandomAccess<FloatType> randomAccess = result.randomAccess();
        while (cursorInput.hasNext()) {
            cursorInput.fwd();
            randomAccess.setPosition(cursorInput);
            if (cursorInput.get().get() == value){
                randomAccess.get().set(setValue);}
        }
    }

    public static Point setOnly(Img<FloatType> masks, IntervalView<FloatType> result, float value, int setValue) {
        int x = 0;
        int y = 0;
        Cursor<FloatType> cursorInput = masks.cursor();
        RandomAccess<FloatType> randomAccess = result.randomAccess();
        while (cursorInput.hasNext()) {
            cursorInput.fwd();
            randomAccess.setPosition(cursorInput);
            if (cursorInput.get().get() == value){
                x+=cursorInput.getIntPosition(0);
                x/=2;
                y+=cursorInput.getIntPosition(1);
                y/=2;
                randomAccess.get().set(setValue);}
            else
                randomAccess.get().set(0);
        }
        return new Point(x,y);
    }

    public static <T extends FloatType> void computeMinMax(
            final Iterable<T> input, final T min, final T max) {
        // create a cursor for the image (the order does not matter)
        final Iterator<T> iterator = input.iterator();

        // initialize min and max with the first image value
        T type = iterator.next();

        min.set(type);
        max.set(type);

        // loop over the rest of the data and determine min and max value
        while (iterator.hasNext()) {
            // we need this type more than once
            type = iterator.next();

            if (type.compareTo(min) < 0)
                min.set(type);

            if (type.compareTo(max) > 0)
                max.set(type);
        }

    }

    public static boolean save(IntervalView<FloatType> view, File file) {
        ImagePlus imp = ImageJFunctions.wrap(view, "Categories");
        imp.show();
        return saveTiffStack(imp, file);
    }

    /*
     * Reimplementation from ImageJ FileSaver class. Necessary since it traverses
     * the entire virtual stack once to collect some slice labels, which takes
     * forever in this case.
     */
    public static boolean saveTiffStack(final ImagePlus imp, final File f) {
        FileInfo fi = imp.getFileInfo();
        boolean virtualStack = imp.getStack().isVirtual();
        if (virtualStack)
            fi.virtualStack = (VirtualStack) imp.getStack();
        fi.info = imp.getInfoProperty();
        fi.description = new FileSaver(imp).getDescriptionString();
        DataOutputStream out = null;
        try {
            TiffEncoder file = new TiffEncoder(fi);
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            file.write(out);
            out.close();
        } catch (IOException e) {
            Log.error(": ERROR: Cannot save file '" + f.getAbsolutePath() + "':" + e);
            return false;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    Log.error(e.toString());
                }
        }
        return true;
    }

    protected static ImagePlus openImp(File f) throws IOException {
        if(!f.exists())
            throw new IOException(String.format("%s File not found",f.getAbsolutePath()));
        ImagePlus imp = new Opener().openImage(f.getAbsolutePath());
        printInfos(imp);
        if (imp.getType()==FLOAT_TYPE)
            return imp;
        else
            return convertImpToFloat(imp);
    }

    public static ImagePlus convertImpToFloat(ImagePlus imp) {
        if(imp.getNDimensions()==2)
        return new ImagePlus("float", imp.getProcessor().convertToFloatProcessor());
        else {
            Log.info(String.format("File have %d dimensions to be converted to Float",imp.getNDimensions()));
            imp.setStack(imp.getStack().convertToFloat());
            return  imp;
        }
    }

//    private static ImagePlus convertMultiDimsImpToFloat(ImagePlus imp) {
//
//        Log.info(String.format("Channels: %d - StackSize: %d",imp.getNChannels(),imp.getStackSize()));
//        Log.info(String.format("Imp: "+Utils.toString(imp.getDimensions())));
//        int nbChannels = imp.getNChannels();
//        Log.info("Stack size: "+  imp.getStack().size());
//        for ( int i = 1; i < imp.getStackSize(); ++i )
//            imp.setStack(imp.getStack().convertToFloat());
//
//      return imp;
////        ImagePlus result = new ImagePlus("float",  imp.getProcessor().convertToFloatProcessor());
////                for ( int i = 1; i < imp.getStackSize(); ++i )
////        {
////            Log.info( Utils.toString(result.getDimensions()));
//////            ImagePlus tmp = new ImagePlus("float",  imp.getStack().getProcessor( i + 1 ).convertToFloatProcessor());
////            result.getStack().addSlice( imp.getStack().getProcessor( i + 1 ).convertToFloatProcessor());
////        }
////        Log.info( Utils.toString(result.getDimensions()));
////        Log.info("Stack size: "+ String.valueOf(result.getStackSize()));
////        result.setDimensions(nchannels, 1, 1);
////
////        Log.info( Utils.toString(result.getDimensions()));
////       return result;
//    }
}
