package net.preibisch.distribution.gui.items;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import net.preibisch.distribution.gui.GUIConfig;

public class PreviewPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;

	private BlocksCanvas canvas;
	
	public PreviewPanel() {
		super();
		setLayout(new GridLayout());
		canvas = new BlocksCanvas();
		canvas.setSize(new Dimension(GUIConfig.PREVIEW_PANEL_WIDTH + 50, GUIConfig.PREIVIEW_PANEL_HEIGHT + 50));
		canvas.setPreferredSize(new Dimension(GUIConfig.PREVIEW_PANEL_WIDTH, GUIConfig.PREVIEW_PREFERED_HEIGHT + 50));
		JScrollPane scrollFrame = new JScrollPane(canvas);
		canvas.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(GUIConfig.PREVIEW_PANEL_WIDTH, GUIConfig.PREIVIEW_PANEL_HEIGHT));
		add(scrollFrame);
		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateCanvas();
			}
		});
		timer.start();
	}

	public void updateCanvas() {
		canvas.update(DataPreview.getBlocksPreview());
		revalidate();
		repaint();
	}

}
