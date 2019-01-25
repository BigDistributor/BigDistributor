package main.java.net.preibisch.distribution.gui.items;

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
		this.setSize(400, 300);
		 logPanel = new LoggingPanel(context);

		this.getContentPane().add(logPanel);		
	}
}
