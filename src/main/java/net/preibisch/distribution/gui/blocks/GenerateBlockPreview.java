package net.preibisch.distribution.gui.blocks;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.preibisch.distribution.gui.items.BlockPreview;
import net.preibisch.distribution.gui.items.basics.Colors;

public interface GenerateBlockPreview {
	
	public static List<BlockPreview> getBlocksPreviewWithOverlap(long[] dimensions, long[] numberBlocks, long[] blocksDimensions,
			int overlap, double perspectiveRation) {

		int[] previewBlockDimensions = new int[blocksDimensions.length];
		for (int i = 0; i < previewBlockDimensions.length; i++)
			previewBlockDimensions[i] = (int) (blocksDimensions[i] * perspectiveRation);

		int[] previewDimensions = new int[dimensions.length];
		for (int i = 0; i < previewDimensions.length; i++)
			previewDimensions[i] = (int) (dimensions[i] * perspectiveRation);

		ArrayList<BlockPreview> blocks = new ArrayList<BlockPreview>();
		int lastBlockXSize = (previewDimensions[0] % previewBlockDimensions[0]) > 0
				? previewDimensions[0] % previewBlockDimensions[0]
				: previewBlockDimensions[0];
		int lastBlockYSize = (previewDimensions[1] % previewBlockDimensions[1]) > 0
				? (previewDimensions[1] % previewBlockDimensions[1])
				: previewBlockDimensions[1];

		for (int i = 0; i < numberBlocks[1]; i++) {
			for (int j = 0; j < numberBlocks[0]; j++) {
				if (i < numberBlocks[1] - 1) {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap,
										previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										previewBlockDimensions[1]),
								Colors.START));
					} else {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										lastBlockXSize + 2 * overlap, previewBlockDimensions[1] + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], lastBlockXSize,
										previewBlockDimensions[1]),
								Colors.START));
					}
				} else {
					if (j < numberBlocks[0] - 1) {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										previewBlockDimensions[0] + 2 * overlap, lastBlockYSize + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], previewBlockDimensions[0],
										lastBlockYSize),
								Colors.START));
					} else {
						blocks.add(new BlockPreview(
								new Rectangle(j * previewBlockDimensions[0], i * previewBlockDimensions[1],
										lastBlockXSize + 2 * overlap, lastBlockYSize + 2 * overlap),
								new Rectangle(overlap + j * previewBlockDimensions[0],
										overlap + i * previewBlockDimensions[1], lastBlockXSize, lastBlockYSize),
								Colors.START));
					}
				}
			}
		}
		return blocks;
	}

	public static List<BlockPreview> getBlocksPreviewWithoutOverlap(long[] graphicBlocks, int computeSizePreviewBox) {
		ArrayList<BlockPreview> blocks = new ArrayList<>();
		int i;
		for (i = 0; i < graphicBlocks[0]; i++) {
			for (int j = 0; j < graphicBlocks[1]; j++) {
				blocks.add(new BlockPreview(null, new Rectangle(j * computeSizePreviewBox, i * computeSizePreviewBox,
						computeSizePreviewBox, computeSizePreviewBox), Colors.START));
			}
		}
		for (int k = 0; k < graphicBlocks[2]; k++) {
			blocks.add(new BlockPreview(null, new Rectangle(k * computeSizePreviewBox, i * computeSizePreviewBox,
					computeSizePreviewBox, computeSizePreviewBox), Colors.START));
		}
		return blocks;
	}
}
