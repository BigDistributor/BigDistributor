package Helpers;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;

public class Portion extends Object {

	private RandomAccessibleInterval<FloatType> view;
	private long[] position;
	private long[] size;
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
	

	public Portion(RandomAccessibleInterval<FloatType> view, long[] position, long[] size) {
		this.view = view;
		this.position = position;
		this.size = size;
		// TODO Auto-generated constructor stub
	}
	
	public Portion(RandomAccessibleInterval<FloatType> view, int dim, int slice) {
		this.view = view;
		this.dimenssion = dim;
		this.slice = slice; 
		// TODO Auto-generated constructor stub
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

	public void setSize(long[] size) {
		this.size = size;
	}

	public RandomAccessibleInterval<FloatType> getView() {
		return view;
	}
	public void setView(RandomAccessibleInterval<FloatType> view) {
		this.view = view;
	}
}

