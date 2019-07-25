package net.preibisch.distribution.gui.items;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class RightPanel extends JPanel{
	private PreviewPanel previewPanel;
	
	public RightPanel() {
		super();
		setLayout(new GridLayout(2, 1));

//		previewPanel = new PreviewPanel(Config.getDataPreview());
		
		
	}
}
