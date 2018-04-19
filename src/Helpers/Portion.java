package Helpers;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class Portion extends Object {

	private RandomAccessibleInterval<FloatType> view;
	private long[] position;
	private long[] size;
	private long[] max;
	public int getSlice() {
		return slice;
	}

	public void setSlice(int slice) {
		this.slice = slice;
	}

	public int getDimenssion() {
		return dimenssion;
	}

	public void setDimenssion(int dimenssion) {
		this.dimenssion = dimenssion;
	}
	private int slice;
	private int dimenssion;
	

	public Portion(RandomAccessibleInterval<FloatType> view, long[] position,long[] max, long[] size) {
		this.view = view;
		this.position = position;
		this.size = size;
		this.max = max;
	}
	
	public Portion(RandomAccessibleInterval<FloatType> view, int dim, int slice) {
		this.view = view;
		this.dimenssion = dim;
		this.slice = slice; 
	}

	public long[] getPosition() {
		return position;
	}

	public void setPosition(long[] position) {
		this.position = position;
	}

	public long[] getSize() {
		return size;
	}

	public long[] getMax() {
		return max;
	}

	public void setMax(long[] max) {
		this.max = max;
	}

	public void setSize(long[] size) {
		this.size = size;
	}

	public RandomAccessibleInterval<FloatType> getView() {
		return view;
	}
	public void setView(RandomAccessibleInterval<FloatType> view) {
		this.view = view;
	}
	@Override
	public String toString() {
		String positionString ="";
		String maxString ="";
		String sizeString ="";
		
		for (int i = 0; i < max.length; i++) {
			positionString+= position[i]+"|";
			maxString+=  max[i]+"|";
			sizeString+=  size[i]+"|";
			
		}
		return "from:"+positionString+" to: "+maxString+ " size:"+sizeString;
	}
}

