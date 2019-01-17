package main.java.com.clustering.workflow;

import java.util.HashMap;
import java.util.List;

import main.java.com.blockmanager.Block;
import main.java.com.gui.items.ProgressBarPanel;

public class Workflow {
	public static List<Block> blocks;
	public static HashMap<Integer, Block> blockMap;
	public static ProgressBarPanel progressBarPanel;

	public Workflow() {
		progressBarPanel = new ProgressBarPanel(0, 100);
	}

}
