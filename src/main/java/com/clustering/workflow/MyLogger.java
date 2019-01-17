package main.java.com.clustering.workflow;

import org.scijava.log.Logger;

import main.java.com.gui.items.LogFrame;
import main.java.com.master.MainFrame;

public class MyLogger {
	
	public static Logger log;
	
	public static void SubLogger() {
		log = MainFrame.getLogService().subLogger("Distribution");
		log.addLogListener(LogFrame.getLogPanel());

//		log.info( "This is an info message" );
//		log.error( "This is an error message" );
//		logPanel = new LoggingPanel(Tr2dContext.ops.context());
	}
}
