package net.preibisch.distribution.gui.items;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UpbarPanel extends JPanel {
	JLabel text;
	JProgressBar bar;
	
	public UpbarPanel() {
		super();
		text = new JLabel("");
		bar = new JProgressBar(0, 100);
	}

}
