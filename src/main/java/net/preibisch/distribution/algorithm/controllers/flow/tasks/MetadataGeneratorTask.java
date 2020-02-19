package net.preibisch.distribution.algorithm.controllers.flow.tasks;

import java.io.File;
import java.util.Map;

import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockGenerator;
import net.preibisch.distribution.algorithm.blockmanagement.blockinfo.BasicBlockInfo;
import net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.algorithm.controllers.items.Job;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.algorithm.controllers.metadata.MetadataGenerator;
import net.preibisch.distribution.gui.items.DataPreview;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

public class MetadataGeneratorTask implements AbstractTask {
	private final static String METADATA_FILE_NAME = "METADATA.json";

	@Override
	public void start(int pos, AbstractCallBack callback) throws Exception {
		callback.log("Creating metadata..");
				
		
//		long[] imgSize = new BoundingBox(bb).getDimensions(1);
//		final Map<Integer, BasicBlockInfo> blocks = BasicBlockGenerator.divideIntoBlocks(blockSize, dims);
//		return new BlocksMetaData(blocks, blockSize, dims,blocks.size());
		BlocksMetaData md = MetadataGenerator.genarateMetaData(DataPreview.getDims(),DataPreview.getBlocksSizes(),DataPreview.getOverlap(), callback);
		File file = new File(Job.getTmpDir(),METADATA_FILE_NAME);
		md.toJson(file);

		callback.onSuccess(pos);
	}

}
