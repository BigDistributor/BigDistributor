package gui.items;

import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;

public class BlockParamsPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5489935889866505715L;
	
	public Scrollbar slider;
	
	public BlockParamsPanel() {
		slider = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 1, 21);
		this.add(slider);
	}

}
