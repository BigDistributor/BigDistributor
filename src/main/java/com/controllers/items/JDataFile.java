package main.java.com.controllers.items;

import main.java.com.imageaccess.Loader;
import main.java.com.tools.Helper;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;

public class JDataFile extends Object{

	final private long[] dimensions;
	final private JFile file;
	final private Loader loader;
	
	
	public Loader getLoader() {
		return loader;
	}
	public long[] getDimensions() {
		return dimensions;
	}
	
	public JFile getFile() {
		return file;
	}
	
	private JDataFile(Builder builder) {
		this.file = builder.file;
		this.dimensions = builder.dimensions;
		this.loader = builder.loader;
	}
	
	public static class Builder{

		private long[] dimensions;
		private JFile file;
		private Loader loader;
		
		public Builder load() {
			this.loader =  Loader.Builder.load(file);
			return this;
		}
		
		public Builder getDataInfos() {
			System.out.println("File to open:"+file.getAll());
//			Img<FloatType> image = IOFunctions.openAs32Bit(new File(file.getAll()));
//			resultImage = new CellImgFactory<FloatType>(64).create(Helper.getDimensions(image),new FloatType());
			RandomAccessibleInterval<FloatType> data;
			try {
				data = loader.fuse();
				dimensions = Helper.getDimensions(data);
			} catch (IncompatibleTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
