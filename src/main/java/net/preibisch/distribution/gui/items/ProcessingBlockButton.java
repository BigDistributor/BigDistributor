package net.preibisch.distribution.gui.items;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ProcessingBlockButton extends JButton {
	private static final long serialVersionUID = -3212552029108193042L;
	private int color;

	public ProcessingBlockButton(int color) {
		super();
		this.color = color;
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((ProcessingBlockButton) e.getSource()).color++;
				((ProcessingBlockButton) e.getSource()).color = ((ProcessingBlockButton) e.getSource()).color % 6;
				System.out.println("New Color: " + ((ProcessingBlockButton) e.getSource()).color);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		this.setBackground(Colors.Color(color));
		super.paintComponent(g);
	}
}
