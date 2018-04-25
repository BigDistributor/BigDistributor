package gui.items;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFrame extends Frame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8610357346945431177L;
	
	public MyFrame(String arg0) {
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
