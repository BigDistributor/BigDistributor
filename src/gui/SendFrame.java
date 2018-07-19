package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import clustering.MyCallBack;
import clustering.WorkflowFunction;
import gui.items.FilePicker;
import gui.items.Frame;
import tools.Config;

public class SendFrame extends Frame{
	private static final long serialVersionUID = 7124828223974163624L;
	FilePicker filePicker ;
	JButton sendButton;
	JButton configButton;
	public SendFrame(String arg0) {
		super(arg0);
		filePicker = new FilePicker("File:", "Browse");
		sendButton = new JButton("Send");
		configButton = new JButton("Config");
		setSize(450, 300);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 20, 30, 20));
		panel.setLayout(new GridLayout(3, 1, 10, 25));
		panel.add(filePicker);
		panel.add(sendButton);
		panel.add(configButton);
		add(panel);
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.setLocalTaskPath(filePicker.getSelectedFilePath());	
				WorkflowFunction workflow = new WorkflowFunction();
				workflow.sendTask(new MyCallBack() {
					
					@Override
					public void onSuccess() {
						JOptionPane.showMessageDialog(null, "File Sent.", "SUCCESS !" , JOptionPane.INFORMATION_MESSAGE);
					}
					
					@Override
					public void onError(String error) {
						JOptionPane.showMessageDialog(null, error, "Error !" , JOptionPane.INFORMATION_MESSAGE);
						
					}
					
					@Override
					public void log(String log) {
						System.out.println(log);
						
					}
				});
				
			}
		});
		configButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFrame configFrame = new ConfigFrame("Cluster Config");
				configFrame.setVisible(true);
				configFrame.setAlwaysOnTop (true);	
			}
		});
	}
	public static void main(String[] args) {
		SendFrame sendFrame = new SendFrame("Input");
		Config.log = new ArrayList<String>();
		sendFrame.setVisible(true);
	}

}
