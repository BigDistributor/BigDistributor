package gui.items;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.JPanel;

public class ProgressBlocksPanel extends JPanel{

	private int sigma ;
	/**
	 * 
	 */
	public ProgressBlocksPanel(int sigma) {
		super();
		this.setLayout(new FlowLayout());
		this.setBackground(GraphicTools.randomColor());
		this.manageBlocksPanel(3, 3);
		this.sigma = sigma;
		
	}
	private static final long serialVersionUID = 4015834098705314055L;

	protected void paintComponent(Graphics g) {
        // call super.paintComponent to get default Swing 
        // painting behavior (opaque honored, etc.)
        super.paintComponent(g);
     
    }
	
	public void manageBlocksPanel(int x, int y) {
		this.removeAll();
		this.setLayout(new GridLayout(x, y));
		for(int i = 1; i <= x ;i++ ) {
			for( int j = 0; j < y ; j++) {
				Label label = new Label();
				label.setText(""+i);
				label.setBackground(GraphicTools.randomColor());
				this.add(label);
			}
		}
		this.revalidate();
		this.repaint();
		
		
	}
}
