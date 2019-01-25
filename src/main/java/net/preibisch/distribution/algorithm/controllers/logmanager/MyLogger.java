package main.java.net.preibisch.distribution.algorithm.controllers.logmanager;

import org.scijava.log.LogService;
import org.scijava.log.Logger;

import main.java.net.preibisch.distribution.gui.items.LogFrame;

public class MyLogger {
	
	public static Logger log;
	
	public static void SubLogger(LogService logService) {
		log = logService.subLogger("Distribution");
		log.addLogListener(LogFrame.getLogPanel());

//		log.info( "This is an info message" );
//		log.error( "This is an error message" );
//		logPanel = new LoggingPanel(Tr2dContext.ops.context());
	}
}
