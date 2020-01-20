package net.preibisch.distribution.tools.helpers;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIHelpers {
	public static JPanel createImagePanel(String path) {
		JPanel panel = new JPanel();
		ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance( 130, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg); 
		JLabel picLabel = new JLabel(imageIcon);
		panel.add(picLabel);
		return panel;
	}
}
