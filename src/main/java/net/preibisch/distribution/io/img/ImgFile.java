package net.preibisch.distribution.io.img;

import java.io.IOException;

import net.preibisch.distribution.io.DataExtension;
import net.preibisch.distribution.io.DistributedFile;
import net.preibisch.distribution.io.FileStatus;


public class ImgFile extends DistributedFile {
	
	protected long[] dims;
	protected DataExtension extension;
	
	public ImgFile (String path,FileStatus mode) throws IOException {
		super(path,mode);
		this.extension = DataExtension.fromURI(path);
	}
	
	public ImgFile (String path) throws IOException {
		this(path,FileStatus.IN_LOCAL_COMPUTER);
	}
	
	public long[] getDims() {
		return dims;
	}
	
	public DataExtension getExtension() {
		return extension;
	}

	public long[] getDimensions() {
		return dims;
	}

	
}
