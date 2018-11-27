package main.java.com.gui.items;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.com.tools.Config;

public class PreviewPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;

	private BlocksCanvas canvas;
	
	public PreviewPanel() {
		super();
		setLayout(new GridLayout());
		canvas = new BlocksCanvas();
		canvas.setSize(new Dimension(Config.PREVIEW_PANEL_WIDTH + 50, Config.PREIVIEW_PANEL_HEIGHT + 50));
		canvas.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.previewPreferedHeight + 50));
		JScrollPane scrollFrame = new JScrollPane(canvas);
		canvas.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.PREIVIEW_PANEL_HEIGHT));
		add(scrollFrame);
	}

	public void updateCanvas() {
		canvas.update(Config.getDataPreview().getBlocksPreview());
		revalidate();
		repaint();
	}

}
