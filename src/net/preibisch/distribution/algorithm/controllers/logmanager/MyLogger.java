package net.preibisch.distribution.algorithm.controllers.logmanager;

import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.log.Logger;

import net.imagej.ops.OpService;
import net.preibisch.distribution.gui.items.LogFrame;

public class MyLogger {
	
	public static Boolean initialized = false;
	
	public static Logger log;
	
	private static OpService opService;
	private static LogService logService;
	
	public static void SubLogger(LogService logService) {
		log = logService.subLogger("Distribution");
		log.addLogListener(LogFrame.getLogPanel());

//		log.info( "This is an info message" );
//		log.error( "This is an error message" );
//		logPanel = new LoggingPanel(Tr2dContext.ops.context());
	}

	public static void initLogger() {
		if(initialized) return ;
		final Context context = new Context( OpService.class, LogService.class );
		opService = context.getService( OpService.class );
		logService = context.getService( LogService.class );
		LogFrame logFrame = new LogFrame(opService.getContext());
		MyLogger.SubLogger(logService);
		logFrame.setVisible(true);
		initialized = true;
	}
}
