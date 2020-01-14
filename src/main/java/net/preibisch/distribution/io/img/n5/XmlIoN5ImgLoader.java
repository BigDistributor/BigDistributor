package net.preibisch.distribution.io.img.n5;

import java.io.File;

import org.jdom2.Element;

import mpicbg.spim.data.generic.sequence.AbstractSequenceDescription;
import mpicbg.spim.data.generic.sequence.ImgLoaderIo;
import mpicbg.spim.data.generic.sequence.XmlIoBasicImgLoader;

@ImgLoaderIo( format = "n5.imgloader", type = N5ImgLoader.class )
public class XmlIoN5ImgLoader implements XmlIoBasicImgLoader< N5ImgLoader >
{

	@Override
	public Element toXml(N5ImgLoader imgLoader, File basePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public N5ImgLoader fromXml(Element elem, File basePath, AbstractSequenceDescription<?, ?, ?> sequenceDescription) {
		// TODO Auto-generated method stub
		return null;
	}

}
