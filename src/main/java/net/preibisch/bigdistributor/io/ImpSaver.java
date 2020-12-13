package net.preibisch.bigdistributor.io;

import ij.ImagePlus;
import ij.VirtualStack;
import ij.io.FileInfo;
import ij.io.FileSaver;
import ij.io.TiffEncoder;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.IntervalView;
import net.preibisch.bigdistributor.tools.helpers.Log;

import java.io.*;

public class ImpSaver {
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
}
