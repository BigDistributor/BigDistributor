package net.preibisch.distribution.gui.items;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Frame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8610357346945431177L;
	
	public Frame(String arg0) {
		super(arg0);
		this.addWindowListener(new WindowAdapter() {
			@Override
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
