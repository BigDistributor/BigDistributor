package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import blockmanager.GraphicBlocksManager;
import gui.items.Frame;
import gui.items.LogPanel;
import gui.items.PrgressParamsPanel;
import gui.items.ProgressPanel;
import tools.Config;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressPanel previewPanel;
	private PrgressParamsPanel blockParamsPanel;
	JProgressBar pbar;

	private LogPanel logPanel;

	public ProgressGUI(String arg0) {
		super(arg0);
		prepareGUI();
	}

	public static void main(String[] args) {
		Config.openInput();
		
		ProgressGUI progressGUI = new ProgressGUI("Progress..");
		progressGUI.setVisible(true);
	}

	private void prepareGUI() {
		setSize(1500, 1000);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new GridBagLayout());
		JPanel progressPreviewPanel = new JPanel();
		progressPreviewPanel.setLayout(new GridLayout(2, 1));

		previewPanel = new ProgressPanel();
		preparePreviewPanel(previewPanel);
		pbar = new JProgressBar(0, 100);
		blockParamsPanel = new PrgressParamsPanel();
		blockParamsPanel.sliderBoxSizePanel.slider.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println(e.getValue());
						Config.setBlocksSize(e.getValue());
						GraphicBlocksManager.updateValues(Config.getDimensions());
						blockParamsPanel.sliderBoxSizePanel.updateValue(e.getValue()+"/"+Config.totalBlocks);
						previewPanel.updateCanvas();
					}
				});
				t.start();
			}
		});
		blockParamsPanel.sliderOverlapPanel.slider.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Config.setOverlap(e.getValue());
				blockParamsPanel.sliderOverlapPanel.updateValue(e.getValue());
				previewPanel.updateCanvas();
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
		logPanel = new LogPanel();

		progressPreviewPanel.add(logPanel);
		c.weightx = 1;
		c.gridx = 1;
		add(blockParamsPanel, c);
		setVisible(true);
		Timer timer = new Timer(10000, new ActionListener() {
		@Override
			public void actionPerformed(ActionEvent e) {
			updateView();
			}
		});
		timer.start();

	}

	private void updateView() {
		previewPanel.canvas.repaint();
		revalidate();
		repaint();

		logPanel.AddText(Config.log);
	}

	private void preparePreviewPanel(ProgressPanel previewPanel2) {
		previewPanel.canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
//				if (e.getX() < graphicSizes[0] && e.getY() < graphicSizes[1]) {
//					int x = (int) ((e.getX() < ((previewPanel.getNumBlocks()[0] - 1) * blockSize))
//							? (e.getX() / blockSize)
//							: previewPanel.getNumBlocks()[0] - 1);
//					int y = (int) ((e.getY() < ((previewPanel.getNumBlocks()[1] - 1) * blockSize))
//							? (e.getY() / blockSize)
//							: previewPanel.getNumBlocks()[1] - 1);
//					int i = (int) (y * (previewPanel.getNumBlocks()[0]) + x);
//					System.out.println("block: " + i);
//					Config.blocksView.get(i).setStatus((Config.blocksView.get(i).getStatus() + 1) % 6);
//				}
			}
		});
	}
}