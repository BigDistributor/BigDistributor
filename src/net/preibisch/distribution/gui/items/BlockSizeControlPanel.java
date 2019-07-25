package net.preibisch.distribution.gui.items;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BlockSizeControlPanel extends JPanel{

	public ArrayList<SliderPanel> sliderPanels;
	
	public BlockSizeControlPanel(int NumControllers) {
		super();
		sliderPanels = new ArrayList<SliderPanel>();
		JPanel contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension( 2000,2000));
		for (int i = 0; i < NumControllers; i++) {
			SliderPanel slider = new SliderPanel(i, "Box Size[" + i + "]:", 100, 2000, 200);
			sliderPanels.add(slider);
		}
		JScrollPane scrollFrame = new JScrollPane(contentPanel);
		contentPanel.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension( 800,300));
		this.add(scrollFrame);
	}
}
