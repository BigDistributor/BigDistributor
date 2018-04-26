package gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import gui.items.BlockParamsPanel;
import gui.items.MyFrame;
import gui.items.ProgressBlocksPanel;
import ij.plugin.PlugIn;

public class ProgressGUI implements PlugIn {

	private MyFrame mainFrame;
	private ProgressBlocksPanel previewPanel;

	@Override
	public void run(String arg) {
		prepareGUI();
	}

	public static void main(String[] args) {
		ProgressGUI progressGUI = new ProgressGUI();
		progressGUI.run("");
	}

	private void prepareGUI() {
		mainFrame = new MyFrame("Progress View");
		mainFrame.setSize(800, 800);
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH); 
		mainFrame.setLayout(new GridBagLayout());

		previewPanel = new ProgressBlocksPanel(8);
		
		BlockParamsPanel blockParamsPanel = new BlockParamsPanel();
		blockParamsPanel.slider.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				System.out.println(e.getValue());;
				previewPanel.manageBlocksPanel(e.getValue(), e.getValue());
			}	
		});
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 3;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		mainFrame.add(previewPanel,c);
		c.weightx = 1;
		c.gridx = 1;
		mainFrame.add(blockParamsPanel,c);
		mainFrame.setVisible(true);
	}
}