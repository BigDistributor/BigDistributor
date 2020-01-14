package net.preibisch.distribution.io.img.n5;

import org.janelia.saalfeldlab.n5.N5Reader;

import mpicbg.spim.data.sequence.ImgLoader;
import mpicbg.spim.data.sequence.SetupImgLoader;

public class N5ImgLoader implements ImgLoader
{
	private String dataset = "/volumes/raw";
	private N5Reader reader;
	
	public N5ImgLoader(N5Reader reader, String dataset) {
		this.reader = reader;
		this.dataset = dataset;
	}
	
	public N5ImgLoader(N5Reader reader) {
		this.reader = reader;
	}
	@Override
	public SetupImgLoader<?> getSetupImgLoader( final int setupId) {
		return new N5SetupImgLoader(reader,dataset);
	}

}
