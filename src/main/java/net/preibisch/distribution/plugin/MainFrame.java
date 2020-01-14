package net.preibisch.distribution.plugin;

import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

import ij.ImageJ;
import net.imagej.ops.OpService;
import net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import net.preibisch.distribution.gui.InputFrame;
import net.preibisch.distribution.gui.items.LogFrame;

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
		LogFrame logFrame = new LogFrame(opService.getContext());
		logFrame.setVisible(true);
		new MainFrame().run();
		
	}

	@Override
	public void run() {
		System.out.println("Hello");
		MyLogger.SubLogger(logService);
		InputFrame inputFrame = new InputFrame("Input");
		inputFrame.setVisible(true);
		
		
	}
}
