package gui.items;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

public class ProgressBlocksPanel extends JPanel {
	private static final long serialVersionUID = 4015834098705314055L;
	private int sigma;
	private Image image;
	public ProgressBlocksPanel(int sigma, Image image) {
		super();
		this.setLayout(new GridLayout());
		this.manageBlocksPanel(3, 3);
		this.sigma = sigma;
		this.image = image;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, sigma, sigma, this);
	}

	public void manageBlocksPanel(int x, int y) {
		this.removeAll();
		this.setLayout(new GridLayout(x, y));
		Random rand = new Random();
		for (int i = 1; i <= x; i++) {
			for (int j = 1; j <= y; j++) {
				int random = rand.nextInt(6);
				ProcessingBlockButton label = new ProcessingBlockButton(random);
				this.add(label);
			}
		}
		this.revalidate();
		this.repaint();
	}
}
