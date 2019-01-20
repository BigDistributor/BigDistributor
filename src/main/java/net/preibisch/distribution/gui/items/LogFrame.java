package main.java.net.preibisch.distribution.gui.items;

import javax.swing.JFrame;

import org.scijava.ui.swing.console.LoggingPanel;

import main.java.net.preibisch.distribution.plugin.MainFrame;

public class LogFrame extends JFrame{
	private static LoggingPanel logPanel;
	
	public static LoggingPanel getLogPanel() {
		return logPanel;
	}
	public LogFrame() {
		super("Log Frame");
		this.setSize(400, 300);
		 logPanel = new LoggingPanel(MainFrame.getOpService().getContext());

		this.getContentPane().add(logPanel);		
	}
}
