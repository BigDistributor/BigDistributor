package gui.items;

import java.awt.GridLayout;

import javax.swing.JPanel;

import blockmanager.BlocksManager;
import tools.Config;

public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;
	public BlocksCanvas canvas;
	private int[] numBlocks;
	private long[] sizes;

	public ProgressPanel(int blockSize, int extra, long[] sizes) {
		super();
		this.sizes = sizes;
		setLayout(new GridLayout());
		updateValues(blockSize, extra, sizes);
		add(canvas);
	
	}
	public void updateCanvas(int blockSize, int extra) {
		updateValues(blockSize, extra, sizes);
		canvas.update(Config.blocksView, blockSize, extra);
	}

	public int[] getNumBlocks() {
		return numBlocks;
	}

	public void setNumBlocks(int[] numBlocks) {
		this.numBlocks = numBlocks;
	}

	public long[] getSizes() {
		return sizes;
	}

	public void setSizes(long[] sizes) {
		this.sizes = sizes;
	}
	private void updateValues(int blockSize, int extra, long[] sizes) {
		numBlocks = new int[]{ (int) sizes[0] / blockSize + ((sizes[0] % blockSize) > 0 ? 1 : 0),
				(int) sizes[1] / blockSize + ((sizes[1] % blockSize) > 0 ? 1 : 0) };
		System.out.println(numBlocks[0]+"-"+numBlocks[1]);
		int BlockSizeInView = Integer.max(5,(int)(600/numBlocks[1]));
		Config.blocksView = BlocksManager.getBlocks(sizes, numBlocks, BlockSizeInView, extra);
		canvas = new BlocksCanvas(null, sizes, numBlocks, blockSize, extra);
	}

}
