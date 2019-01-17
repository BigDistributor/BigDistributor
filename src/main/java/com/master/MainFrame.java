package main.java.com.master;

import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

import ij.ImageJ;
import main.java.com.clustering.workflow.MyLogger;
import main.java.com.gui.InputFrame;
import main.java.com.gui.items.LogFrame;
import mpicbg.spim.io.IOFunctions;
import net.imagej.ops.OpService;

public class MainFrame implements Command {
	
	@Parameter
	private static OpService opService;
	
	@Parameter
	private static LogService logService;

	public static LogService getLogService() {
		return logService;
	}
	
	public static OpService getOpService() {
		return opService;
	}
	
	public static void main(String[] args) {
		new ImageJ();
		final Context context = new Context( OpService.class, LogService.class );
		opService = context.getService( OpService.class );
		logService = context.getService( LogService.class );
		LogFrame logFrame = new LogFrame();
		logFrame.setVisible(true);
		new MainFrame().run();
		
	}

	@Override
	public void run() {
		System.out.println("Hello");
		MyLogger.SubLogger();
		InputFrame inputFrame = new InputFrame("Input");
		inputFrame.setVisible(true);
		
		
	}
}
