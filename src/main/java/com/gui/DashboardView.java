package main.java.com.gui;

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

import main.java.com.blockmanager.GraphicBlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.gui.items.Frame;
import main.java.com.gui.items.ControlPanel;
import main.java.com.gui.items.PreviewPanel;
import main.java.com.gui.items.SliderPanel;
import main.java.com.gui.items.UpbarPanel;
import main.java.com.tools.Config;

public class DashboardView extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private static final int[] FRAME_SIZE= {1500,1000};
	private UpbarPanel upbarPabel;
	private PreviewPanel previewPanel;
	private ControlPanel controlPanel;
	


	public DashboardView(String arg0) {
		super(arg0);
//		JPanel progressPreviewPanel = new JPanel(new GridLayout(2, 1));
		setSize(FRAME_SIZE[0],FRAME_SIZE[1]);
		setLayout(new GridBagLayout());
		previewPanel = new PreviewPanel();
		controlPanel = new ControlPanel(Config.getJob().getInput().getDimensions().length);
		for(SliderPanel slider: controlPanel.blockSizeControlPanel.sliderPanels) {
		slider.slider.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println(e.getValue());
						Config.getDataPreview().setBlockSize(slider.getId(),e.getValue());
						Config.getDataPreview().generateBlocks();
						slider.updateValue(e.getValue());
//						controlPanel.numberBlocksLabel.setText("Total Blocks:"+Config.totalBlocks);

						Config.getDataPreview().generateBlocks();
						previewPanel.updateCanvas();
					}
				});
				t.start();
			}
		});
		}
		controlPanel.sliderOverlapPanel.slider.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Config.getDataPreview().setOverlap(e.getValue());
				Config.getDataPreview().generateBlocks();
				controlPanel.sliderOverlapPanel.updateValue(e.getValue());

				Config.getDataPreview().generateBlocks();
				previewPanel.updateCanvas();
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 3;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(previewPanel, c);
//		progressPreviewPanel.add(previewPanel);
	

//		progressPreviewPanel.add(controlPanel.workflow.logPanel);
		c.weightx = 1;
		c.gridx = 1;
		add(controlPanel, c);
		setVisible(true);
		Timer timer = new Timer(1000, new ActionListener() {
		@Override
			public void actionPerformed(ActionEvent e) {
			updateView();
			}
		});
		timer.start();

	}

	private void updateView() {
		previewPanel.updateCanvas();
		revalidate();
		repaint();
	}
}