package gui.items;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import blockmanager.GraphicBlocksManager;
import tools.Config;

public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;

	public BlocksCanvas canvas;

	public ProgressPanel() {
		super();
		setLayout(new GridLayout());
		GraphicBlocksManager.updateValues(Config.getDimensions());
		canvas = new BlocksCanvas();
		canvas.setSize(new Dimension(Config.PREVIEW_PANEL_WIDTH + 50, Config.PREIVIEW_PANEL_HEIGHT + 50));
		canvas.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.previewPreferedHeight + 50));
		JScrollPane scrollFrame = new JScrollPane(canvas);
		canvas.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.PREIVIEW_PANEL_HEIGHT));
		add(scrollFrame);
	}

	public void updateCanvas() {
		GraphicBlocksManager.updateValues(Config.getDimensions());
		canvas.update(Config.blocksView);
	}

}
