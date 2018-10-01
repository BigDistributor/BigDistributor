package main.java.com.gui.items;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.com.blockmanager.GraphicBlocksManager;
import main.java.com.clustering.MyCallBack;
import main.java.com.tools.Config;

public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;

	public BlocksCanvas canvas;

	public ProgressPanel() {
		super();
		setLayout(new GridLayout());
		GraphicBlocksManager.updateValues(Config.getDimensions(), new MyCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void log(String log) {
				// TODO Auto-generated method stub
				
			}
		});
		canvas = new BlocksCanvas();
		canvas.setSize(new Dimension(Config.PREVIEW_PANEL_WIDTH + 50, Config.PREIVIEW_PANEL_HEIGHT + 50));
		canvas.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.previewPreferedHeight + 50));
		JScrollPane scrollFrame = new JScrollPane(canvas);
		canvas.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(Config.PREVIEW_PANEL_WIDTH, Config.PREIVIEW_PANEL_HEIGHT));
		add(scrollFrame);
	}

	public void updateCanvas() {
		GraphicBlocksManager.updateValues(Config.getDimensions(), new MyCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void log(String log) {
				// TODO Auto-generated method stub
				
			}
		});
		canvas.update(Config.blocksView);
	}

}
