package main.java.com.tools;

import java.io.File;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class JDataFile {

	final private long[] dimensions;
	final private JFile file;
	
	
	public long[] getDimensions() {
		return dimensions;
	}
	
	public JFile getFile() {
		return file;
	}
	
	private JDataFile(Builder builder) {
		this.file = builder.file;
		this.dimensions = builder.dimensions;
	}
	
	public static class Builder{

		private long[] dimensions;
		private JFile file;
		
		public Builder getDataInfos() {
			Img<FloatType> image = IOFunctions.openAs32Bit(new File(file.getAll()));
//			resultImage = new CellImgFactory<FloatType>(64).create(Helper.getDimensions(image),new FloatType());
			dimensions = Helper.getDimensions(image);
			return this;
		}
		
		public Builder file(JFile file) {
			this.file = file;
			return this;
		}
		

		public JDataFile build() {
			return new JDataFile(this);
		}
		
	}
	
}
