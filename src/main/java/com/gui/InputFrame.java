package main.java.com.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.com.gui.items.FilePicker;
import main.java.com.gui.items.Frame;
import main.java.com.tools.Config;

public class InputFrame extends Frame {
	private static final long serialVersionUID = -6778326835751228740L;
	FilePicker jarPicker;
	FilePicker inputPicker;
	JButton nextButton;
	JButton configButton;

	public InputFrame(String arg0) {
		super(arg0);
		jarPicker = new FilePicker("Jar:", "Browse");
		inputPicker = new FilePicker("Input:", "Browse");
		nextButton = new JButton("Next");
		nextButton.setMnemonic(KeyEvent.VK_ENTER);
		configButton = new JButton("Config");
		configButton.setMnemonic(KeyEvent.VK_C);
		setSize(450, 300);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(4, 1, 10, 25));
		panel.add(jarPicker);
		panel.add(inputPicker);
		panel.add(nextButton);
		panel.add(configButton);
		add(panel);
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((!jarPicker.getSelectedFilePath().isEmpty()) && (!inputPicker.getSelectedFilePath().isEmpty())) {
					Config.setLocalTaskPath(jarPicker.getSelectedFilePath());
					Config.setOriginalInputFilePath(inputPicker.getSelectedFilePath());
					setVisible(false);
					dispose();
					Config.init();
					ProgressGUI progressGUI = new ProgressGUI("Progress..");
					progressGUI.setVisible(true);
				}
			}
		});
		configButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFrame configFrame = new ConfigFrame("Cluster Config");
				configFrame.setVisible(true);
				configFrame.setAlwaysOnTop(true);
			}
		});
	}

}
