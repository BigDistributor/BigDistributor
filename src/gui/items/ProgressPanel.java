package gui.items;

import java.awt.GridLayout;

import javax.swing.JPanel;

import blockmanager.BlocksManager;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import tools.Config;
import tools.Helper;

public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = -5153593379781883390L;
	private static final long minimumBoxSize = 50;
	private static final long panelWidth = 800;
	private static final long panelHeight = 500;
	public BlocksCanvas canvas;
	private long[] numberBlocks;
	private long[] dimensions;
	private long[] graphicBlocks;
	private double perspectiveRation;

	public ProgressPanel() {
		/*
		 * JFrame frame = new JFrame("slider Bar Example");
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.setSize(new
		 * Dimension(900, 400)); JPanel test = new JPanel(); test.setPreferredSize(new
		 * Dimension( 2000,500)); JScrollPane scrollFrame = new JScrollPane(test);
		 * test.setAutoscrolls(true); scrollFrame.setPreferredSize(new Dimension(
		 * 800,300)); frame.add(scrollFrame); // frame.setContentPane(it); //
		 * frame.pack(); frame.setVisible(true);
		 * 
		 */

		super();
		this.dimensions = Helper.getDimensions(Config.getInputFile());
		updateValues();
		canvas = new BlocksCanvas();
		setLayout(new GridLayout());
		
		add(canvas);

	}

	public void updateCanvas() {
		updateValues();
		canvas.update(Config.blocksView);
	}

	private void updateValues() {
		Helper.log(dimensions.length+"D input");
		if (dimensions.length == 2) {
			numberBlocks = new long[2];
			for (int i = 0; i < 2; i++)
				numberBlocks[i] = dimensions[i] / Config.getBlockSize(i)
						+ ((dimensions[i] % Config.getBlockSize(i)) > 0 ? 1 : 0);
			perspectiveRation = computeRationView(numberBlocks, Config.getBlocksSize());
			Helper.log("Blocks:"+numberBlocks[0]+"-"+numberBlocks[1]+" Perspective:"+perspectiveRation);
		
			Config.blocksView = BlocksManager.getBlocks(dimensions, numberBlocks, Config.getBlocksSize(),
			Config.getOverlap(), perspectiveRation);
			} else {
			graphicBlocks = computeGraphicBlocks(dimensions, Config.getBlocksSize());
			Config.blocksView = BlocksManager.getBlocks(graphicBlocks, computeSizePreviewBox(graphicBlocks));
		}
		System.out.println("updated view");
	}

	private double computeRationView(long[] numberBlocks, long[] blocksSize) {
		Helper.log("Panel Width:"+panelWidth+" NumberBlocks:"+numberBlocks[0]+" BlocksSize:"+blocksSize[0]);
		return panelWidth / (numberBlocks[0] * blocksSize[0]);
	}

	private long[] computeBlocksPerDimension(long[] dimensions, long[] blocksDimensions) {
		long[] numberBlocksPerDimension = new long[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			numberBlocksPerDimension[i] = dimensions[i] / blocksDimensions[i]
					+ ((dimensions[i] % blocksDimensions[i]) > 0 ? 1 : 0);
		}
		return numberBlocksPerDimension;
	}

	private long coutTotalBlocks(long[] numberBlocksPerDimension) {
		long totalBlocks = 1;
		for (long elm : numberBlocksPerDimension)
			totalBlocks *= elm;
		return totalBlocks;
	}

	// return 3D long [1]-columns [2]-Rows [3]-Columns in last row
	private long[] computeGraphicBlocks(long[] dimensions, long[] blocksDimensions) {
		long[] numberBlocksPerDimension = computeBlocksPerDimension(dimensions, blocksDimensions);
		long totalBlocks = coutTotalBlocks(numberBlocksPerDimension);
		long sqrt = (long) Math.sqrt(totalBlocks);
		if (sqrt * minimumBoxSize < panelWidth) {
			return new long[] { sqrt, sqrt, totalBlocks % sqrt };
		} else {
			long columns = panelHeight / minimumBoxSize;
			long rows = totalBlocks / columns;
			long rest = totalBlocks % columns;
			return new long[] { columns, rows, rest };
		}
	}

	private int computeSizePreviewBox(long[] numberPreviewBlocks) {
		return (int) (panelHeight / numberPreviewBlocks[0]);
	}

	public static long[] get2DDimensions(Img<FloatType> file) {
		long[] dims = Helper.getDimensions(file);
		String logString = Helper.logArray(dims);
		Helper.log("Dims: " + logString);
		for (int i = 2; i < dims.length; i++) {
			dims[i % 2] = dims[i % 2] * dims[i];
		}
		long[] result = new long[] { dims[0], dims[1] };
		logString = Helper.logArray(result);
		Helper.log("2D - Dims: " + logString);
		return result;
	}
}
