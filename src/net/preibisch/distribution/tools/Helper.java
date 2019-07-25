package net.preibisch.distribution.tools;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.imglib2.RandomAccessibleInterval;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;

public class Helper {
	

	public static <T> long[] getDimensions(RandomAccessibleInterval<T> image) {
		long[] dimensions = new long[image.numDimensions()];
		for (int d = 0; d < image.numDimensions(); ++d) {
			dimensions[d] = image.dimension(d);
			System.out.println(d + " - " + image.dimension(d));
		}
		return dimensions;
	}

	public static List<String[]> generateBlocksPerJob(String[] localBlocksfiles, int jobs, AbstractCallBack callBack) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		int part = localBlocksfiles.length / jobs;
		int rest = localBlocksfiles.length % jobs;
		int i=0;
		if (part > 0) {
			for (i = 0; i < jobs - 1; i++) {
				String[] temp = Arrays.copyOfRange(localBlocksfiles, i * part, (i + 1) * part);
				callBack.log("Job " + (i + 1) + ":[" + (i * part) + "-" + ((i + 1) * part - 1) + "|" + temp.length + "/"
						+ localBlocksfiles.length + "]:" + String.join("|", temp));
				list.add(temp);
			}
		}
		String[] restArray = Arrays.copyOfRange(localBlocksfiles, i * part, i * part + rest);
		callBack.log("with Rest: " + String.join("|", restArray));
		list.add(restArray);

		// list.add(ObjectArrays.concat(temp, restArray, String.class));
		return list;

	}

	public static String logArray(long[] dims) {
		String log = "";
		for (long elm : dims)
			log += elm + ",";
		return log;
	}
	
	public static ArrayList<String> getFiles(String path, String prefix) {
		File folder = new File(path);
		String[] files = folder.list();
		ArrayList<String> result = new ArrayList<>();
		for (String file: files) {
			if (file.endsWith(prefix)) result.add(file);
		}
		return result;
	}

	public static JPanel createImagePanel(String path) {
		JPanel panel = new JPanel();
		ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance( 130, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg); 
		JLabel picLabel = new JLabel(imageIcon);
		panel.add(picLabel);
		return panel;
	}

}
