package gui.items;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tools.Config;

public class LogPanel extends JPanel {
	private static final long serialVersionUID = 1661293578856881139L;
	private JTextArea txtarea;

	public LogPanel() {
		super();
		Config.log = new ArrayList<>();
		setLayout(new BorderLayout());
		txtarea = new JTextArea();
		txtarea.setEditable(false);
		txtarea.setPreferredSize(new Dimension(0, 300));
		txtarea.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(txtarea);
		add(scroll, BorderLayout.PAGE_START);
	}

	public void addText(String log) {
		txtarea.setText(log+"\n"+txtarea.getText());
	}

}
