package gui;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import blockmanager.BlocksManager;
import gui.items.BlockView;
import gui.items.BlocksCanvas;
import gui.items.Frame;

public class DrawView extends Frame {
	private static final long serialVersionUID = -1607170881678219251L;

	public DrawView(String arg0) {
		super(arg0);
		setLayout(new GridLayout());
		setSize(500, 600);
	}

	public static void main(String[] args) {
		try {

			DrawView drawView = new DrawView("Hello");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout());
			String string = "img/image.jpg";
			Image image = ImageIO.read(new File(string));
			int blockSize = 300;
			int extra = 8;
			long[] sizes = { image.getWidth(null), image.getHeight(null) };
			int[] numBlocks = { (int) sizes[0] / blockSize + ((sizes[0] % blockSize) > 0 ? 1 : 0),
					(int) sizes[1] / blockSize + ((sizes[1] % blockSize) > 0 ? 1 : 0) };
			final ArrayList<BlockView> blocks = BlocksManager.getBlocks(sizes, numBlocks, blockSize, extra);
			BlocksCanvas canvas = new BlocksCanvas(null, sizes, numBlocks, blockSize, blocks, extra);
			panel.add(canvas);
			drawView.add(panel);
//			drawView.getContentPane().add(canvas);
			drawView.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}