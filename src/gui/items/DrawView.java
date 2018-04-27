package gui.items;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
			String string = "img/image.jpg";
			Image image = ImageIO.read(new File(string));
			int x = 5;
			int y = 8;
			int sigma = 8;
			final ArrayList<BlockView> blocks = getBlocks(image, x, y, sigma);

			BlocksCanvas canvas = new BlocksCanvas(image, blocks, sigma);
			drawView.getContentPane().add(canvas);
			drawView.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<BlockView> getBlocks(Image image, int x, int y, int sigma) {
		ArrayList<BlockView> blocks = new ArrayList<BlockView>();
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		Params.blocksX = x;
		Params.blocksY = y;
		Params.blockXSize = width / Params.blocksX;
		Params.blockYSize = height / Params.blocksY;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				blocks.add(new BlockView(
						new Rectangle(j * Params.blockXSize, i * Params.blockYSize, Params.blockXSize + 2 * sigma, Params.blockYSize + 2 * sigma),
						new Rectangle(sigma + j * Params.blockXSize, sigma + i * Params.blockYSize, Params.blockXSize, Params.blockYSize),
						(j * i) % 6));
			}
		}
		return blocks;
	}
}