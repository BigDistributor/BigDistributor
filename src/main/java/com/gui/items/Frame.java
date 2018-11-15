package main.java.com.gui.items;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import ij.process.AutoThresholder.Method;

public class Frame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8610357346945431177L;
	
	public Frame(String arg0) {
		super(arg0);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				close();
				System.exit(0);
			}
		});
	}

	private void close() {
		System.out.println("Did close !");		
	}
}
