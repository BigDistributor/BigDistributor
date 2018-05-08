package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.items.BlocksCanvas;
import gui.items.Frame;
import gui.items.PrgressParamsPanel;
import gui.items.ProgressPanel;
import tools.Config;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressPanel previewPanel;
	private PrgressParamsPanel blockParamsPanel;
	 long[] sizes;
	private int blockSize;
	private int extra;

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
			blockSize=100;
			extra=8;
			sizes = new long[]{image.getWidth(null),image.getHeight(null)};
			previewPanel = new ProgressPanel(blockSize, extra, sizes);
			preparePreviewPanel(previewPanel);
			previewPanel.setSize(image.getWidth(null) + 2 * sigma, image.getHeight(null) + 2 * sigma);
			blockParamsPanel = new PrgressParamsPanel();
			blockParamsPanel.sliderX.addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					System.out.println(e.getValue());
					Config.setBlockSize( e.getValue());;
					previewPanel.updateCanvas((int) Config.getBlockSize(),Config.getSigma());
				}
			});
			blockParamsPanel.sliderY.addAdjustmentListener(new AdjustmentListener() {
				
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					Config.setSigma(e.getValue());;
					previewPanel.updateCanvas((int) Config.getBlockSize(),Config.getSigma());
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

	private void preparePreviewPanel(ProgressPanel previewPanel2) {
		previewPanel.canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getX() < sizes[0] && e.getY() < sizes[1]) {
					int x = (int) ((e.getX() < ((previewPanel.getNumBlocks()[0] - 1) * blockSize)) ? (e.getX() / blockSize)
							: previewPanel.getNumBlocks()[0] - 1);
					int y = (int) ((e.getY() < ((previewPanel.getNumBlocks()[1] - 1) * blockSize)) ? (e.getY() / blockSize)
							: previewPanel.getNumBlocks()[1] - 1);
					int i = (int) (y * (previewPanel.getNumBlocks()[0]) + x);
					System.out.println("block: " + i);
					previewPanel.getBlocks().get(i).setStatus((previewPanel.getBlocks().get(i).getStatus() + 1) % 6);
					((BlocksCanvas) e.getSource()).repaint();
					revalidate();
					repaint();
				}
			}
		});
		
	}
}