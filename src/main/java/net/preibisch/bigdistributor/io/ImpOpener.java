package net.preibisch.bigdistributor.io;

import java.io.File;
import java.io.IOException;

import ij.ImagePlus;
import ij.io.Opener;
import net.preibisch.bigdistributor.tools.helpers.Log;
import net.preibisch.bigdistributor.tools.helpers.Utils;

public class ImpOpener {

    private static final int FLOAT_TYPE = 2;
    
	public static ImagePlus openImp(File f) throws IOException {
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
	
    public static void printInfos(ImagePlus imp) {
        Log.info(imp.getFileInfo().toString());
        Log.info("Stack: " + imp.getStack().size());
        Log.info("Channel: " + imp.getChannel());
        Log.info("Dims: "+ Utils.toString(imp.getDimensions()));
        Log.info("Type: " + imp.getType());
    }
}
