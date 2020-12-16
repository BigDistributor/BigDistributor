package net.preibisch.bigdistributor.gui;

import net.preibisch.bigdistributor.gui.items.DataPreview;
import net.preibisch.bigdistributor.gui.items.Frame;
import net.preibisch.bigdistributor.gui.items.PreviewPanel;

public class PreviewUI extends Frame {
	private static final long serialVersionUID = 1L;
	
	private final static String TITLE = "Progress proview..";
	private PreviewPanel panel;
	long[] dims;
//	private int size;

	public PreviewUI(long[] dims) {
		super(TITLE);
		this.dims = dims;
		DataPreview.fromFile(dims);
		setSize(GUIConfig.PREVIEW_PANEL_WIDTH, GUIConfig.PREIVIEW_PANEL_HEIGHT);
		panel = new PreviewPanel();
		add(panel);
		setVisible(true);
	}




	public static void main(String[] args)  {
//		String input_path = "/Users/Marwan/Desktop/Task/data/hdf5/dataset.xml";
//		XMLFile file = XMLFile.create(input_path, Double.NaN,FileStatus.IN_LOCAL_COMPUTER);
//		PreviewUI ui = new PreviewUI(file.getDimensions(),1);

		long[] dims = new long[]{800,800};
		new PreviewUI(dims);

	}
}
