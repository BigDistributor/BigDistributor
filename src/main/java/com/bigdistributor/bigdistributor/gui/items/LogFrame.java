package net.preibisch.bigdistributor.gui.items;

import org.scijava.Context;
import org.scijava.ui.swing.console.LoggingPanel;

import javax.swing.*;

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
