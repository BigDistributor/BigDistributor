package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import gui.items.Frame;
import gui.items.PrgressParamsPanel;
import gui.items.ProgressPanel;
import tools.Config;
import tools.Helper;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressPanel previewPanel;
	private PrgressParamsPanel blockParamsPanel;
	JProgressBar pbar;
	long[] graphicSizes;
	private int blockSize;
	private int extra;
	private int numBlocks;
	private LogPanel logPanel;

	public ProgressGUI(String arg0) {
		super(arg0);
		prepareGUI();
	}

	public static void main(String[] args) {
		ProgressGUI progressGUI = new ProgressGUI("Progress..");
		progressGUI.setVisible(true);
	}

	private void prepareGUI() {

		setSize(1300, 800);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new GridBagLayout());
		JPanel progressPreviewPanel = new JPanel();
		progressPreviewPanel.setLayout(new GridLayout(2, 1));
		blockSize = 100;
		extra = 8;

		graphicSizes = Helper.get2DDimensions(Config.getInputFile());
		previewPanel = new ProgressPanel(blockSize, extra, graphicSizes);
		preparePreviewPanel(previewPanel);
//		previewPanel.setSize(600, 400);
		numBlocks = Config.blocksView.size();
		pbar = new JProgressBar(0, numBlocks);
		blockParamsPanel = new PrgressParamsPanel();
		blockParamsPanel.sliderX.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				System.out.println(e.getValue());
				Config.setBlockSize(e.getValue());
				;
				previewPanel.updateCanvas((int) Config.getBlockSize(), Config.getOverlap());
			}
		});
		blockParamsPanel.sliderY.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Config.setOverlap(e.getValue());
				previewPanel.updateCanvas((int) Config.getBlockSize(), Config.getOverlap());
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
		Timer timer = new Timer(2000, new ActionListener() {
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
				if (e.getX() < graphicSizes[0] && e.getY() < graphicSizes[1]) {
					int x = (int) ((e.getX() < ((previewPanel.getNumBlocks()[0] - 1) * blockSize))
							? (e.getX() / blockSize)
							: previewPanel.getNumBlocks()[0] - 1);
					int y = (int) ((e.getY() < ((previewPanel.getNumBlocks()[1] - 1) * blockSize))
							? (e.getY() / blockSize)
							: previewPanel.getNumBlocks()[1] - 1);
					int i = (int) (y * (previewPanel.getNumBlocks()[0]) + x);
					System.out.println("block: " + i);
					Config.blocksView.get(i).setStatus((Config.blocksView.get(i).getStatus() + 1) % 6);
				}
			}
		});
	}

	class LogPanel extends JPanel {
		private static final long serialVersionUID = 1661293578856881139L;
		private JTextArea txtarea;

		public LogPanel() {
			super();
			setLayout(new BorderLayout());
			txtarea = new JTextArea();
			txtarea.setEditable(false);
			txtarea.setPreferredSize(new Dimension(0, 300));
			txtarea.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(txtarea);
			add(scroll, BorderLayout.PAGE_START);
		}

		public void AddText(ArrayList<String> log) {
			txtarea.setText(String.join("\n", log));
			log = new ArrayList<String>();

		}
	}

}