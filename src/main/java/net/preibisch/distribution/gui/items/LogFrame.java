package net.preibisch.distribution.gui.items;

import javax.swing.JFrame;

import org.scijava.Context;
import org.scijava.ui.swing.console.LoggingPanel;

public class LogFrame extends JFrame{
	private static LoggingPanel logPanel;
	
	public static LoggingPanel getLogPanel() {
		return logPanel;
	}
	public LogFrame(Context context) {
		super("Log Frame");
		this.setSize(900, 400);
		 logPanel = new LoggingPanel(context);

		this.getContentPane().add(logPanel);		
	}
}
