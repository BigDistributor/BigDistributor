package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import gui.items.Frame;
import gui.items.PrgressParamsPanel;
import gui.items.ProgressPanel;
import tools.Config;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressPanel previewPanel;
	private PrgressParamsPanel blockParamsPanel;
	JProgressBar pbar;
	long[] sizes;
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
		
		int sigma = 8;
		setSize(800, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new GridBagLayout());
		JPanel progressPreviewPanel = new JPanel();
		progressPreviewPanel.setLayout(new GridLayout(2, 1));
		String string = "img/image.jpg";
		Image image;

		try {
			image = ImageIO.read(new File(string));
			blockSize = 100;
			extra = 8;
			sizes = new long[] { image.getWidth(null), image.getHeight(null) };
			previewPanel = new ProgressPanel(blockSize, extra, sizes);
			preparePreviewPanel(previewPanel);
			previewPanel.setSize(image.getWidth(null) + 2 * sigma, image.getHeight(null) + 2 * sigma);
			numBlocks = Config.blocksView.size();
			pbar = new JProgressBar(0, numBlocks);
			blockParamsPanel = new PrgressParamsPanel();
			blockParamsPanel.sliderX.addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					System.out.println(e.getValue());
					Config.setBlockSize(e.getValue());
					;
					previewPanel.updateCanvas((int) Config.getBlockSize(), Config.getSigma());
				}
			});
			blockParamsPanel.sliderY.addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					Config.setSigma(e.getValue());
					previewPanel.updateCanvas((int) Config.getBlockSize(), Config.getSigma());
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
		} catch (IOException e1) {e1.printStackTrace();}
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
				if (e.getX() < sizes[0] && e.getY() < sizes[1]) {
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
			setLayout(new GridLayout());
//			GridBagConstraints c = new GridBagConstraints();
			txtarea = new JTextArea();
			txtarea.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(txtarea);
			add(scroll);
		}

		public void AddText(ArrayList<String> log) {
			
			txtarea.setText(String.join("\n",log));
//				txtarea.getDocument().insertString(0, String.join("\n",log), null);
			log = new ArrayList<String>();
			
		}
	}

}