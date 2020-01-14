package net.preibisch.distribution.io.img.n5;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import mpicbg.spim.data.generic.sequence.ImgLoaderHint;
import mpicbg.spim.data.sequence.SetupImgLoader;
import mpicbg.spim.data.sequence.VoxelDimensions;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.real.FloatType;

public class N5SetupImgLoader<T  extends NativeType<T>> implements SetupImgLoader<T>//MultiResolutionSetupImgLoader<T>
{
	private String dataset ;
	private N5Reader reader;

	public N5SetupImgLoader(N5Reader reader, String dataset) {
		this.reader = reader;
		this.dataset = dataset; 
		
	}

	@Override
	public RandomAccessibleInterval<T> getImage(int timepointId, ImgLoaderHint... hints) {
		RandomAccessibleInterval<T> virtual = null;
		try {
			virtual = N5Utils.open(reader, dataset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return virtual;
	}

	@Override
	public T getImageType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomAccessibleInterval<FloatType> getFloatImage(int timepointId, boolean normalize,
			ImgLoaderHint... hints) {
		// can leave it for now
		try {
			return N5Utils.open(reader, dataset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR! file not found");
		}
		return null;
	}

	@Override
	public Dimensions getImageSize(int timepointId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoxelDimensions getVoxelSize(int timepointId) {
		// TODO Auto-generated method stub
		return null;
	}
}
