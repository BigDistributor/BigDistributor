package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.items.ProgressBlocksPanel;


public class TwoPanels {
    public static void main(String[] args) {

        JPanel p = new JPanel();
        // setting layout to null so we can make panels overlap
        p.setLayout(null);

        ProgressBlocksPanel topPanel = new ProgressBlocksPanel(8);
        // drawing should be in blue
        topPanel.setForeground(Color.blue);
        // background should be black, except it's not opaque, so 
        // background will not be drawn
        topPanel.setBackground(Color.black);
        // set opaque to false - background not drawn
        topPanel.setOpaque(false);
        topPanel.setBounds(50, 50, 100, 100);
        // add topPanel - components paint in order added, 
        // so add topPanel first
        p.add(topPanel);
        TwoPanels.managePanel(topPanel, 3, 2);

        JPanel bottomPanel = new JPanel();
        // drawing in green
        bottomPanel.setForeground(Color.green);
        // background in cyan
        bottomPanel.setBackground(Color.red);
        // and it will show this time, because opaque is true
        bottomPanel.setOpaque(true);
        bottomPanel.setBounds(30, 30, 100, 100);
        // add bottomPanel last...
        p.add(bottomPanel);

        // frame handling code...
        JFrame f = new JFrame("Two Panels");
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static void managePanel(JPanel topPanel, int x, int y) {
		topPanel.removeAll();
		topPanel.setLayout(new GridLayout(x, y));
		for(int i = 1; i <= x ;i++ ) {
			for( int j = 0; j < y ; j++) {
				Label label = new Label();
				label.setText(""+i);
				label.setBackground(randomColor());
				topPanel.add(label);
			}
		}
		topPanel.revalidate();
		topPanel.repaint();
	}
    private static Color randomColor() {
    	System.out.println((int)(Math.random() * 0x1000000));
		return new Color((int)(Math.random() * 0x1000000));
	}
 
}
