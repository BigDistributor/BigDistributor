package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.items.PrgressParamsPanel;
import gui.items.Frame;
import gui.items.ProgressBlocksPanel;
import tools.Config;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressBlocksPanel previewPanel;

	public ProgressGUI(String arg0) {
		super(arg0);
		prepareGUI();
	}

	public static void main(String[] args) {
		ProgressGUI progressGUI = new ProgressGUI("Progress..");
		progressGUI.setVisible(true);
	}

	private void prepareGUI() {
		int sigma = 8;
		setSize(800, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new GridBagLayout());
		JPanel progressPreviewPanel = new JPanel();
		progressPreviewPanel.setLayout(null);
		String string = "img/image.jpg";
		Image image;
		try {
			image = ImageIO.read(new File(string));
			previewPanel = new ProgressBlocksPanel(sigma, image);
			previewPanel.setSize(image.getWidth(null) + 2 * sigma, image.getHeight(null) + 2 * sigma);
			PrgressParamsPanel blockParamsPanel = new PrgressParamsPanel();
			blockParamsPanel.sliderX.addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					System.out.println(e.getValue());
					Config.getNumberBlocks()[0] = e.getValue();
					previewPanel.manageBlocksPanel(Config.getNumberBlocks());
				}
			});
			blockParamsPanel.sliderY.addAdjustmentListener(new AdjustmentListener() {
				
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					Config.getNumberBlocks()[1] = e.getValue();
					previewPanel.manageBlocksPanel(Config.getNumberBlocks());
				}
			});
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 3;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			add(progressPreviewPanel, c);
			progressPreviewPanel.add(previewPanel);
			c.weightx = 1;
			c.gridx = 1;
			add(blockParamsPanel, c);
			setVisible(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}