package gui;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.items.Frame;
import tools.Config;

public class ConfigFrame extends Frame {
	private static final long serialVersionUID = -1404300721421953024L;
	TextField hostField;
	TextField portField;
	TextField pseudoField;
	JButton saveButton;
	JButton resetButton;

	public ConfigFrame(String arg0) {
		super(arg0);
		this.setSize(450, 300);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(4, 2, 10, 25));
		hostField = new TextField("maxlogin2.mdc-berlin.net");
		portField = new TextField("22");
		pseudoField = new TextField("mzouink");
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");
		panel.add(new Label("Host:"));
		panel.add(hostField);
		panel.add(new Label("Port:"));
		panel.add(portField);
		panel.add(new Label("Pseudo:"));
		panel.add(pseudoField);
		panel.add(saveButton);
		panel.add(resetButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.setHost(hostField.getText());
				int port = 22;
				try {
					port = Integer.valueOf(portField.getText());
				}catch(Exception err) {
					System.out.println("Error! Invalide Port");
				}
				Config.setPort(port);
				Config.setPseudo(pseudoField.getText() );
				System.out.println(portField.getText()+"-"+
				Config.getHost()+" - "+Config.getPort()+" - "+Config.getPseudo());
				setVisible(false); 
				dispose();
			}
		});
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hostField.setText("");
				portField.setText("");
				pseudoField.setText("");
			}
		});
		this.add(panel);
	}

	public static void main(String[] args) {
		ConfigFrame configFrame = new ConfigFrame("Cluster Config");
		configFrame.setVisible(true);
	}
}
