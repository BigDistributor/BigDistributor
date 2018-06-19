package gui.items;

import java.awt.GridLayout;

import javax.swing.JPanel;

import blockmanager.GraphicBlocksManager;
import tools.Config;

public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;

	public BlocksCanvas canvas;


	public ProgressPanel() {
		
		 
//		  frame.setSize(new Dimension(900, 400)); 
//		  JPanel test = new JPanel(); 
//		  test.setPreferredSize(new Dimension( 2000,500)); 
//		  JScrollPane scrollFrame = new JScrollPane(test);
//		 test.setAutoscrolls(true); scrollFrame.setPreferredSize(new Dimension(800,300)); frame.add(scrollFrame);
//		 frame.setContentPane(it); 
//		 frame.pack(); frame.setVisible(true);
		 

		super();
		GraphicBlocksManager.updateValues(Config.getDimensions());
		canvas = new BlocksCanvas();
		setLayout(new GridLayout());
		
		add(canvas);

	}

	public void updateCanvas() {
		GraphicBlocksManager.updateValues(Config.getDimensions());
		canvas.update(Config.blocksView);
	}



	
}
