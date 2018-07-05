package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import blockmanager.GraphicBlocksManager;
import clustering.MyCallBack;
import gui.items.Frame;
import gui.items.PrgressParamsPanel;
import gui.items.ProgressPanel;
import gui.items.SliderPanel;
import tools.Config;

public class ProgressGUI extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private ProgressPanel previewPanel;
	private PrgressParamsPanel blockParamsPanel;
	JProgressBar pbar;


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
		pbar = new JProgressBar(0, 100);
		blockParamsPanel = new PrgressParamsPanel();
		for(SliderPanel slider: blockParamsPanel.sliderBoxSizePanel) {
		slider.slider.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println(e.getValue());
						Config.setBlocksSize(e.getValue(), slider.getId());
						GraphicBlocksManager.updateValues(Config.getDimensions(), new MyCallBack() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onError(String error) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void log(String log) {
								// TODO Auto-generated method stub
								
							}
						});
						slider.updateValue(e.getValue());
						blockParamsPanel.numberBlocksLabel.setText("Total Blocks:"+Config.totalBlocks);
						previewPanel.updateCanvas();
					}
				});
				t.start();
			}
		});
		}
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
	

		progressPreviewPanel.add(blockParamsPanel.workflow.logPanel);
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
	}
}