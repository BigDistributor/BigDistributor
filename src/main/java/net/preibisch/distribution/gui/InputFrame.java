package main.java.net.preibisch.distribution.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.gui.items.FilePicker;
import main.java.net.preibisch.distribution.gui.items.Frame;
import mpicbg.spim.data.SpimDataException;

public class InputFrame extends Frame implements ActionListener {
	private static final long serialVersionUID = -6778326835751228740L;
	private FilePicker taskPicker, inputPicker, extraPicker;
	private JButton nextButton, configButton;
	private JRadioButton remoteInputButton, localInputButton;
	private AppMode appMode = AppMode.LOCAL_INPUT_MODE;
	private boolean configurated = false;

	public InputFrame(String arg0) {
		super(arg0);
		MyLogger.log.info("Hello");
		initConfig();
		initGui();
	}

	private static void initConfig() {
		Login.login();
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

		taskPicker = new FilePicker("Jar:", "Browse");

		extraPicker = new FilePicker("Extra:", "Browse");
		inputPicker = new FilePicker("Input:", "Browse");
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
				try {
					Job.initJob(appMode, taskPath, inputPath);
				} catch (SpimDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				DataPreview.fromFile(Job.getInput());

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
			appMode = AppMode.LOCAL_INPUT_MODE;
		}

		if (e.getSource() == remoteInputButton) {
			inputPicker.hideButton();
			appMode = AppMode.CLUSTER_INPUT_MODE;
		}
	}

}
