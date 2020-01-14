package net.preibisch.distribution.gui.items;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBarPanel extends JPanel {
	private static final long serialVersionUID = 869204808437674538L;
	JProgressBar pbar;
	 private int min = 0;
	 private int max = 100;

	public ProgressBarPanel() {
		init();
	}

	public ProgressBarPanel(int min, int max) {
		this.min = min;
		this.max = max;
		init();
	  }

	  public void updateBar(int newValue) {
	    pbar.setValue(newValue);
	  }
	  
	  private void init() {
		  JLabel label = new JLabel("Progress:");
			pbar = new JProgressBar();
			pbar.setMinimum(min);
			pbar.setMaximum(max);
			add(label);
			add(pbar);
	  }

	  public static void main(String args[]) {

	    final ProgressBarPanel it = new ProgressBarPanel();

	    JFrame frame = new JFrame("Progress Bar Example");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setContentPane(it);
	    frame.pack();
	    frame.setVisible(true);

	    // run a loop to demonstrate raising
	    for (int i = 0; i <= 100; i++) {
	      final int percent = i;
	      try {
	        SwingUtilities.invokeLater(new Runnable() {
	          @Override
			public void run() {
	            it.updateBar(percent);
	          }
	        });
	        java.lang.Thread.sleep(100);
	      } catch (InterruptedException e) {
	        ;
	      }
	    }
	  }
	}
