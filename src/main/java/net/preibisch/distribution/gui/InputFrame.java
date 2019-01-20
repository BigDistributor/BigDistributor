package main.java.net.preibisch.distribution.gui;

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

import main.java.net.preibisch.distribution.algorithm.controllers.items.AppMode;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JDataFile;
import main.java.net.preibisch.distribution.algorithm.controllers.items.Job;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Account;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.ServerConfiguration;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.gui.items.FilePicker;
import main.java.net.preibisch.distribution.gui.items.Frame;
import main.java.net.preibisch.distribution.tools.Config;


public class InputFrame extends Frame implements ActionListener {
	private static final long serialVersionUID = -6778326835751228740L;
	private FilePicker taskPicker, inputPicker,extraPicker;
	private JButton nextButton, configButton;
	private JRadioButton remoteInputButton, localInputButton;
	private AppMode appMode = AppMode.LocalInputMode;
	private boolean configurated = false;

	public InputFrame(String arg0) {
		super(arg0);
		MyLogger.log.info("Hello");
		Account account = new Account.Builder().build();
		ServerConfiguration server = new ServerConfiguration.Builder().build();
		Login login = new Login.Builder().id().server(server).account(account).build();
		Config.setLogin(login);
		
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
			if(!configurated) {
				JOptionPane.showMessageDialog(null, "Configurate first." , "Not configured ! using default configuration" , JOptionPane.INFORMATION_MESSAGE);
				  
			}
//			else 
			if ((!taskPicker.getSelectedFilePath().isEmpty()) && (!inputPicker.getSelectedFilePath().isEmpty())) {

				setVisible(false);
				
				
				JDataFile inputData = new JDataFile.Builder()
													.file(inputPicker.getFile())
													.load()
													.getDataInfos()
													.build();
				
//				for (final ViewId viewid: inputData.getLoader().getSpimData().getSequenceDescription().getViewDescriptions().values())	
//					IOFunctions.println(viewid);
				
				
				Job job = new Job.Builder()
							     .appMode(appMode)
							     .task(taskPicker.getFile())
							     .extra(extraPicker.getFile())
							     .input(inputData)
							     .buid();
				
				
				
				DataPreview dataPreview = new DataPreview.Builder()
														 .file(inputData)
														 .build();
				dataPreview.generateBlocks();
				
				Config.setJob(job);
				Config.setDataPreview(dataPreview);
				
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
			appMode = AppMode.LocalInputMode;
		}

		if (e.getSource() == remoteInputButton) {
			inputPicker.hideButton();
			appMode = AppMode.ClusterInputMode;
		}
	}
	
//	public static void main(String[] args) {
//		InputFrame inputFrame = new InputFrame("Input");
//		inputFrame.taskPicker.setText("/home/mzouink/Desktop/Task/gaussian.jar");
//		inputFrame.inputPicker.setText( "/home/mzouink/Desktop/example_dataset/dataset.xml" );
//		inputFrame.nextButton.doClick();
//	}

}
