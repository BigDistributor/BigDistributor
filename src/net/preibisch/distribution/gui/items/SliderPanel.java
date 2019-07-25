package net.preibisch.distribution.gui.items;

import java.awt.Scrollbar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SliderPanel extends JPanel {
	private static final long serialVersionUID = 7244656178595333291L;
	private int id;
	private JLabel label;
	private JLabel valueLabel;
	public Scrollbar slider;

	public void updateValue(int value) {
		valueLabel.setText(String.valueOf(value));
	}

	public SliderPanel() {
		super();
	}

	public SliderPanel(int id,String labelText, int min, int max, int def) {
		super();
		label = new JLabel(labelText);
		valueLabel = new JLabel(String.valueOf(def));
		slider = new Scrollbar(Scrollbar.HORIZONTAL, def, 1, min, max);
		this.id = id;
		this.add(label);
		this.add(slider);
		this.add(valueLabel);
	}
	
	public static void main(String[] args) {
		 final SliderPanel it = new SliderPanel(-1,"test:",20,500,50);

		    JFrame frame = new JFrame("slider Bar Example");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setContentPane(it);
		    frame.pack();
		    frame.setVisible(true);
	}

//	public void updateValue(String string) {
//		valueLabel.setText(string);
//	}
	
	public int getId() {
		return id;
	}
}
