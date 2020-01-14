package net.preibisch.distribution.gui;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import net.preibisch.distribution.algorithm.clustering.jsch.TCPProperties;
import net.preibisch.distribution.algorithm.clustering.kafka.KafkaProperties;
import net.preibisch.distribution.algorithm.controllers.items.server.Login;
import net.preibisch.distribution.gui.items.Frame;
import net.preibisch.distribution.tools.config.server.Account;
import net.preibisch.distribution.tools.config.server.ServerConfiguration;

public class ConfigFrame extends Frame {
	private static final long serialVersionUID = -1404300721421953024L;
	TextField hostField;
	TextField portField;
	TextField pseudoField;
	TextField bufferSizeField;
	TextField kafkaAddressField;
	JPasswordField passwordField;
	TextField serverPathField;
	JButton saveButton;
	JButton resetButton;

	public ConfigFrame(String arg0) {
		super(arg0);
		this.setSize(450, 350);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(6, 2, 10, 25));
		hostField = new TextField("maxlogin2.mdc-berlin.net");
		portField = new TextField("22");
		pseudoField = new TextField("mzouink");
		passwordField = new JPasswordField();
		serverPathField = new TextField("/fast/AG_Preibisch/Marwan/clustering/");
		bufferSizeField = new TextField(String.valueOf(TCPProperties.BUFFER_SIZE));
		kafkaAddressField = new TextField(KafkaProperties.KAFKA_SERVER_URL);
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");
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
		panel.add(new Label("TCP Buffer Size:"));
		panel.add(bufferSizeField);
		panel.add(new Label("Kafka Server IP:"));
		panel.add(kafkaAddressField);
		panel.add(saveButton);
		panel.add(resetButton);
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				KafkaProperties.setURL(kafkaAddressField.getText());
				try {
					TCPProperties.setBufferSize(Integer.parseInt(bufferSizeField.getText()));
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Invalid Buffer size", "InfoBox: " + "ERROR !", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Account account = new Account(pseudoField.getText(), passwordField.getPassword());

				int portValue = 22;

				try {
					portValue = Integer.valueOf(portField.getText());
				} catch (Exception err) {

					JOptionPane.showMessageDialog(null, "Invalid port", "InfoBox: " + "ERROR !", JOptionPane.ERROR_MESSAGE);
					return;
				}

				ServerConfiguration server = new ServerConfiguration.Builder().host(hostField.getText()).port(portValue)
						.path(serverPathField.getText()).build();
				Login.login(server, account);

//				System.out.println(Login.toString());

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
