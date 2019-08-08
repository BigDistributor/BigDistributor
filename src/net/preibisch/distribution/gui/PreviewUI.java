package net.preibisch.distribution.gui;

import java.io.IOException;

import mpicbg.spim.data.SpimDataException;
import net.preibisch.distribution.gui.items.DataPreview;
import net.preibisch.distribution.gui.items.Frame;
import net.preibisch.distribution.gui.items.PreviewPanel;
import net.preibisch.distribution.io.img.ImgFile;
import net.preibisch.distribution.io.img.XMLFile;

public class PreviewUI extends Frame {
	private static final long serialVersionUID = 1L;
	
	private final static String TITLE = "Progress proview..";
	private PreviewPanel panel;
	private int size;

	public PreviewUI(ImgFile file, int size) {
		super(TITLE);
		this.size = size;
		DataPreview.fromFile(file);
		setSize(GUIConfig.PREVIEW_PANEL_WIDTH, GUIConfig.PREIVIEW_PANEL_HEIGHT);
		panel = new PreviewPanel();
		add(panel);
		setVisible(true);
	}
	
	public PreviewUI(ImgFile file) {
		this(file, 1);
	}

	public static void main(String[] args) throws SpimDataException, IOException {
		String input_path = "/Users/Marwan/Desktop/grid-3d-stitched-h5/dataset.xml";
		XMLFile file = XMLFile.XMLFile(input_path);
		PreviewUI ui = new PreviewUI(file);

	}
}
