package main.java.com.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import ij.ImageJ;
import main.java.com.controllers.items.AppMode;
import main.java.com.controllers.items.JDataFile;
import main.java.com.controllers.items.Job;
import main.java.com.gui.items.DataPreview;
import main.java.com.gui.items.FilePicker;
import main.java.com.gui.items.Frame;
import main.java.com.tools.Config;
import mpicbg.spim.data.sequence.ViewId;
import mpicbg.spim.io.IOFunctions;


public class InputFrame extends Frame implements ActionListener {
	private static final long serialVersionUID = -6778326835751228740L;
	private FilePicker taskPicker, inputPicker;
	private JButton nextButton, configButton;
	private JRadioButton remoteInputButton, localInputButton;
	private AppMode appMode = AppMode.LocalInputMode;
	private boolean configurated = false;

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
			if(!configurated) {
				JOptionPane.showMessageDialog(null, "Configurate first." , "Configuration needed !" , JOptionPane.INFORMATION_MESSAGE);
				  
			}else if ((!taskPicker.getSelectedFilePath().isEmpty()) && (!inputPicker.getSelectedFilePath().isEmpty())) {
				new ImageJ();
				IOFunctions.printIJLog = true;
				setVisible(false);
				
				
				JDataFile inputData = new JDataFile.Builder()
													.file(inputPicker.getFile())
													.getDataInfos()
													.load()
													.build();
				
				for (final ViewId viewid: inputData.getLoader().getSpimData().getSequenceDescription().getViewDescriptions().values())
					IOFunctions.println(viewid);
				
				Job job = new Job.Builder()
							     .appMode(appMode)
							     .task(taskPicker.getFile())
							     .input(inputData)
							     .buid();
				
				
				
				DataPreview dataPreview = new DataPreview.Builder()
														 .file(inputData)
														 .build();
				dataPreview.generateBlocks();
				
				Config.setJob(job);
				Config.setDataPreview(dataPreview);
				
				dispose();
				DashboardView progressGUI = new DashboardView("Progress..");
				progressGUI.setVisible(true);
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
			appMode = AppMode.LocalInputMode;
		}

		if (e.getSource() == remoteInputButton) {
			inputPicker.hideButton();
			appMode = AppMode.ClusterInputMode;
		}
	}

}
