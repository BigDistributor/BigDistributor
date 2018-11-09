package main.java.com.gui;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import main.java.com.gui.items.Frame;
import main.java.com.tools.Config;
import main.java.com.controllers.items.server.Account;
import main.java.com.controllers.items.server.Login;
import main.java.com.controllers.items.server.ServerConfiguration;

public class ConfigFrame extends Frame {
	private static final long serialVersionUID = -1404300721421953024L;
	TextField hostField;
	TextField portField;
	TextField pseudoField;
	JPasswordField passwordField;
	TextField serverPathField;
	JButton saveButton;
	JButton resetButton;

	public ConfigFrame(String arg0) {
		super(arg0);
		this.setSize(450, 300);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(6, 2, 10, 25));
		hostField = new TextField("maxlogin2.mdc-berlin.net");
		portField = new TextField("22");
		pseudoField = new TextField("mzouink");
		passwordField = new JPasswordField();
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");
		serverPathField = new TextField("/fast/AG_Preibisch/Marwan/clustering/");
		panel.add(new Label("Host:"));
		panel.add(hostField);
		panel.add(new Label("Port:"));
		panel.add(portField);
		panel.add(new Label("Pseudo:"));
		panel.add(pseudoField);
		panel.add(new Label("Password:"));
		panel.add(passwordField);
		panel.add(new Label("Cluster Path:"));
		panel.add(serverPathField);
		panel.add(saveButton);
		panel.add(resetButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Account account = new Account.Builder().pseudo(pseudoField.getText())
						.password(String.valueOf(passwordField.getPassword())).build();

				int portValue = 22;

				try {
					portValue = Integer.valueOf(portField.getText());
				} catch (Exception err) {
					System.out.println("Error! Invalide Port");
				}

				ServerConfiguration server = new ServerConfiguration.Builder().host(hostField.getText()).port(portValue)
						.path(serverPathField.getText()).build();

				Login login = new Login.Builder().id().server(server).account(account).build();

				System.out.println(login.toString());
				Config.setLogin(login);

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
