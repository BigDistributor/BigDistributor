package main.java.com.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import main.java.com.gui.items.FilePicker;
import main.java.com.gui.items.Frame;
import main.java.com.tools.AppMode;
import main.java.com.tools.Config;

public class InputFrame extends Frame implements ActionListener {
	private static final long serialVersionUID = -6778326835751228740L;
	FilePicker taskPicker, inputPicker;
	JButton nextButton, configButton;
	JRadioButton remoteInputButton, localInputButton;

	public InputFrame(String arg0) {
		super(arg0);
		
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
		inputPicker = new FilePicker("Input:", "Browse");
		nextButton = new JButton("Next");
		nextButton.setMnemonic(KeyEvent.VK_ENTER);
		nextButton.addActionListener(this);
		configButton = new JButton("Config");
		configButton.setMnemonic(KeyEvent.VK_C);
		configButton.addActionListener(this);

		setSize(450, 330);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(5, 1, 10, 25));
		panel.add(taskPicker);
		panel.add(inputPicker);
		panel.add(boxesPanel);

		panel.add(nextButton);
		panel.add(configButton);
		add(panel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextButton) {
			if ((!taskPicker.getSelectedFilePath().isEmpty()) && (!inputPicker.getSelectedFilePath().isEmpty())) {
				Config.setLocalTaskPath(taskPicker.getSelectedFilePath());
				Config.setOriginalInputFilePath(inputPicker.getSelectedFilePath());
				setVisible(false);
				dispose();
				Config.init();
				ProgressGUI progressGUI = new ProgressGUI("Progress..");
				progressGUI.setVisible(true);
			}
		}

		if (e.getSource() == configButton) {
			ConfigFrame configFrame = new ConfigFrame("Cluster Config");
			configFrame.setVisible(true);
			configFrame.setAlwaysOnTop(true);
		}

		if (e.getSource() == localInputButton) {
			inputPicker.showButton();
			Config.APP_MODE = AppMode.LocalInputMode;
		}

		if (e.getSource() == remoteInputButton) {
			inputPicker.hideButton();
			Config.APP_MODE = AppMode.ClusterInputMode;
		}
	}

}
