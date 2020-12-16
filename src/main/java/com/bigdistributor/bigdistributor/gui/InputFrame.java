package net.preibisch.bigdistributor.gui;

import net.preibisch.bigdistributor.algorithm.clustering.server.Connection;
import net.preibisch.bigdistributor.algorithm.controllers.items.Job.DataAccessMode;
import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import net.preibisch.bigdistributor.gui.items.FilePicker;
import net.preibisch.bigdistributor.gui.items.Frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class InputFrame extends Frame implements ActionListener {
	private static final long serialVersionUID = -6778326835751228740L;
	private FilePicker taskPicker, inputPicker, extraPicker;
	private JButton nextButton, configButton;
	private JRadioButton remoteInputButton, localInputButton;
	private DataAccessMode appMode = DataAccessMode.SEND_DATA_FROM_LOCAL;
	private boolean configurated = false;

	public InputFrame(String arg0) {
		super(arg0);
		MyLogger.log().info("Hello");
		initConfig();
		initGui();
	}

	private static void initConfig() {
		Connection.login();
	}

	private void initGui() {
		localInputButton = new JRadioButton("Local Input");
		localInputButton.setMnemonic(KeyEvent.VK_L);
		localInputButton.setSelected(true);
		localInputButton.addActionListener(this);

		remoteInputButton = new JRadioButton("Remote Input");
		remoteInputButton.setMnemonic(KeyEvent.VK_R);
		remoteInputButton.addActionListener(this);

		ButtonGroup inputGroup = new ButtonGroup();
		inputGroup.add(localInputButton);
		inputGroup.add(remoteInputButton);

		JPanel boxesPanel = new JPanel();
		boxesPanel.add(localInputButton);
		boxesPanel.add(remoteInputButton);

		taskPicker = new FilePicker("Jar:");

		extraPicker = new FilePicker("Extra:");
		inputPicker = new FilePicker("Input:");
		nextButton = new JButton("Next");
		nextButton.setMnemonic(KeyEvent.VK_ENTER);
		nextButton.addActionListener(this);
		configButton = new JButton("Config");
		configButton.setMnemonic(KeyEvent.VK_C);
		configButton.addActionListener(this);

		setSize(450, 400);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(6, 1, 10, 25));
		panel.add(taskPicker);
		panel.add(extraPicker);
		panel.add(inputPicker);
		panel.add(boxesPanel);

		panel.add(nextButton);
		panel.add(configButton);
		add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextButton) {
			if (!configurated) {
				JOptionPane.showMessageDialog(null, "Configurate first.",
						"Not configured ! using default configuration", JOptionPane.INFORMATION_MESSAGE);

			}
//			else 
			if ((!taskPicker.getFile().isEmpty()) && (!inputPicker.getFile().isEmpty())) {

				setVisible(false);

				String inputPath = inputPicker.getFile();
				String taskPath = taskPicker.getFile();

//				DataPreview.fromFile(Job.getInput());

				dispose();
				DashboardView dashboardView = new DashboardView("Progress..");
				dashboardView.setVisible(true);
			}
		}

		if (e.getSource() == configButton) {
			configurated = true;
			ConfigFrame configFrame = new ConfigFrame("Cluster Config");
			configFrame.setVisible(true);
			configFrame.setAlwaysOnTop(true);
		}

		if (e.getSource() == localInputButton) {
			inputPicker.showButton();
			appMode = DataAccessMode.SEND_DATA_FROM_LOCAL;
		}

		if (e.getSource() == remoteInputButton) {
			inputPicker.hideButton();
			appMode = DataAccessMode.READY_IN_CLUSTER_INPUT;
		}
	}

}
