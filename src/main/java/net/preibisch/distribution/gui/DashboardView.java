package net.preibisch.distribution.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.Timer;

import net.preibisch.distribution.gui.items.ControlPanel;
import net.preibisch.distribution.gui.items.DataPreview;
import net.preibisch.distribution.gui.items.Frame;
import net.preibisch.distribution.gui.items.PreviewPanel;
import net.preibisch.distribution.gui.items.SliderPanel;
import net.preibisch.distribution.gui.items.UpbarPanel;

public class DashboardView extends Frame {
	private static final long serialVersionUID = -667700225183799945L;
	private static final int[] FRAME_SIZE = { 1500, 1000 };
	private UpbarPanel upbarPabel;
	private PreviewPanel previewPanel;
	private ControlPanel controlPanel;

	public DashboardView(String arg0) {
		super(arg0);
		setSize(FRAME_SIZE[0], FRAME_SIZE[1]);
		setLayout(new GridBagLayout());
		previewPanel = new PreviewPanel();
		controlPanel = new ControlPanel(3);
		for (SliderPanel slider : controlPanel.blockSizeControlPanel.sliderPanels) {
			slider.slider.addAdjustmentListener(new SliderListener());
		}
		controlPanel.sliderOverlapPanel.slider.addAdjustmentListener(new SliderListener());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 3;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(previewPanel, c);
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

	class SliderListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if(e.getSource().equals(controlPanel.sliderOverlapPanel.slider)) {
				DataPreview.setOverlap(e.getValue());
				DataPreview.generateBlocks();
				controlPanel.sliderOverlapPanel.updateValue(e.getValue());

				DataPreview.generateBlocks();
				previewPanel.updateCanvas();
			}else {
				 if (e.getSource() instanceof SliderPanel)
				 {
					 SliderPanel slider = (SliderPanel) e.getSource();
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						System.out.println(e.getValue());
						DataPreview.setBlockSize(slider.getId(),e.getValue());
						DataPreview.generateBlocks();
						slider.updateValue(e.getValue());
//						controlPanel.numberBlocksLabel.setText("Total Blocks:"+Config.totalBlocks);

						DataPreview.generateBlocks();
						previewPanel.updateCanvas();
					}
				});
				t.start();}
			}
			
		}

	}

}