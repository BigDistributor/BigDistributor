package gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ConfigFrame extends MyFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1404300721421953024L;
	
	public ConfigFrame(String arg0) {
		super(arg0);
		this.setSize(400, 400);
//		this.setLayout();
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 10, 30, 10));
		panel.setSize(250, 350);
		panel.setLayout(new GridLayout(4, 2,10,10));
		panel.add(new Label("Host:"));
		panel.add(new TextField("maxlogin2.mdc-berlin.net"));
		panel.add(new Label("Port:"));
		panel.add(new TextField("22"));
		panel.add(new Label("Pseudo:"));
		panel.add(new TextField("mzouink"));
		panel.add(new JButton("Save"));
		panel.add(new JButton("Reset"));
		this.add(panel);
	}
	
	public static void main(String[] args) {
		ConfigFrame configFrame = new ConfigFrame("Cluster Config");
		configFrame.setVisible(true);
	}
}
