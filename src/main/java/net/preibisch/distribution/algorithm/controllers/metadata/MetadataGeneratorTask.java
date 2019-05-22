package main.java.net.preibisch.distribution.algorithm.controllers.metadata;

import java.io.File;
import java.io.IOException;

import main.java.net.preibisch.distribution.algorithm.controllers.items.AbstractTask;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.gui.items.DataPreview;
import main.java.net.preibisch.distribution.tools.config.Config;

public class MetadataGeneratorTask implements AbstractTask {
	private final static String METADATA_FILE_NAME = "METADATA.json";

	@Override
	public void start(int pos, AbstractCallBack callback) throws IOException {
		callback.log("Creating metadata..");
		final DataPreview data = Config.getDataPreview();
		
		BlocksMetaData md = MetadataGenerator.genarateMetaData(data.getFile().getDims(),data.getBlocksSizes(),data.getOverlap(), callback);
		File file = new File(Config.getJob().getTmpDir(),METADATA_FILE_NAME);
		JsonMetadata.createJSon(md, file , callback);
		
		callback.onSuccess(pos);
	}

}
