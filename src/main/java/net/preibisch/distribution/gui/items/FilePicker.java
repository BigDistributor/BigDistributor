package main.java.net.preibisch.distribution.gui.items;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import main.java.net.preibisch.distribution.algorithm.controllers.items.JExtension;
import main.java.net.preibisch.distribution.algorithm.controllers.items.JFile;
import main.java.net.preibisch.distribution.tools.Config;

public class FilePicker extends JPanel {
	private static final long serialVersionUID = 8143576502972745045L;
	private JLabel label;
	private JTextField textField;
	private JButton button;
	private JFile file;
	private JFileChooser fileChooser;

	public FilePicker(String textFieldLabel, String buttonLabel) {
		fileChooser = new JFileChooser(Config.getDefaultInputPath());
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		label = new JLabel(textFieldLabel);
		textField = new JTextField(20);
		button = new JButton(buttonLabel);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				buttonActionPerformed(evt);
			}
		});
		add(label);
		add(textField);
		add(button);
	}

	private void buttonActionPerformed(ActionEvent evt) {
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			String extension = "";
			String name = "";
			int i = fileChooser.getSelectedFile().getName().lastIndexOf('.');
			if (i > 0) {
			    extension = fileChooser.getSelectedFile().getName().substring(i+1);
			    name = fileChooser.getSelectedFile().getName().substring(0, i);
			}
			file = new JFile.Builder()
					.all(fileChooser.getSelectedFile().getAbsolutePath())
					.path(fileChooser.getSelectedFile().getParent())
					.name(name)
					.extension(JExtension.fromString(extension))
					.build();
			
			System.out.println(file.toString());
			
			
			
		}
	}

	public void addFileTypeFilter(String extension, String description) {
		FileTypeFilter filter = new FileTypeFilter(extension, description);
		fileChooser.addChoosableFileFilter(filter);
	}

	public String getSelectedFilePath() {
		return textField.getText();
	}
	
	public JFile getFile() {
		return file;
	}

	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
	
	public void hideButton() {
		button.setVisible(false);
	}
	
	public void showButton() {
		button.setVisible(true);
	}
	
	public void setText(String string) {
		this.textField.setText(string);
	}

	public class FileTypeFilter extends FileFilter {
		private String extension;
		private String description;
		public FileTypeFilter(String extension, String description) {
			this.extension = extension;
			this.description = description;
		}

		@Override
		public String getDescription() {
			return description + String.format(" (*%s)", extension);
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			return f.getName().toLowerCase().endsWith(extension);
		}
	}
}
