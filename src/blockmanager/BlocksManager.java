package blockmanager;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.google.common.io.Files;

import gui.items.BlockView;
import gui.items.Colors;
import ij.ImageJ;
import multithreading.Threads;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import tools.Config;
import tools.Helper;
import tools.IOFunctions;

public class BlocksManager
{
	public static void main( String[] args ) 
	{
		
		new ImageJ();
		
		IOFunctions.cleanFolder("input");
		String string = "img/DrosophilaWing.tif";
		Img<FloatType> image = IOFunctions.openAs32Bit( new File( string) );
		ImageJFunctions.show(image);
		final long blockSize = 50;
		final List< Block > blocks = generateBlocks(image, blockSize, Config.getOverlap());
		final HashMap< Integer, Block > blockMap = saveBlocks(image,blocks);
		final Img<FloatType> resultImage = new CellImgFactory< FloatType >( 64 ).create(Helper.getDimensions(image), new FloatType());
		generateResult(blockMap,Config.getInputTempDir());
		ImageJFunctions.show(resultImage);		
		final Img<FloatType> resultImage2 = image.copy();
		try { Gauss3.gauss( Config.getOverlap(), Views.extendMirrorSingle( resultImage2 ), resultImage2 ); } catch (IncompatibleTypeException e) {}
		ImageJFunctions.show(resultImage2);
		System.out.println( imageDifference( resultImage, resultImage2 ) );
	}

	public static void generateResult( HashMap<Integer, Block> blockMap, String blocksDir) {
		Img<FloatType> image = IOFunctions.openAs32Bit( new File( Config.getOriginalInputFilePath()) );
		final Img<FloatType> resultImage = new CellImgFactory< FloatType >( 64 ).create(Helper.getDimensions(image), new FloatType());
		for ( final Integer key : blockMap.keySet() )
		{
			String string =blocksDir+"/"+key+".tif";
			Img<FloatType> tmp = IOFunctions.openAs32Bit( new File( string) );
			blockMap.get( key ).pasteBlock( resultImage, tmp );			
		}
		ImageJFunctions.show(resultImage).setTitle("Result");
		
	}

	public static HashMap<Integer, Block> saveBlocks(Img<FloatType> image, List<Block> blocks) {
		final long[] blockSizeDim = Util.getArrayFromValue( Config.getBlockSize(), image.numDimensions() );
		final Img< FloatType > tmp = ArrayImgs.floats( blockSizeDim );
		final RandomAccessible< FloatType > infiniteImg = Views.extendMirrorSingle( image );
		int i = 0;
		final HashMap< Integer, Block > blockMap = new HashMap<>();
		File tempDir = Files.createTempDir();
		Config.setInputTempDir(tempDir.getAbsolutePath());
		Helper.log("Temp Dir: "+tempDir.getAbsolutePath());
		for ( final Block block : blocks )
		{
			++i;
			block.copyBlock( infiniteImg, tmp );
			blockMap.put( i, block );
			IOFunctions.saveTiffStack( IOFunctions.getImagePlusInstance( tmp ), tempDir.getAbsolutePath()+"/"+i+".tif" );
			Config.progressValue = (i*100) / blocks.size();
		}
		return blockMap;
	}

	public static double imageDifference( final RandomAccessibleInterval< FloatType> img1, final RandomAccessibleInterval< FloatType > img2 )
	{
		final Cursor<FloatType> c1 = Views.iterable( img1 ).localizingCursor();
		final RandomAccess<FloatType> r2 = img2.randomAccess();
		double sumChange = 0;
		while ( c1.hasNext() )
		{
			c1.fwd();
			r2.setPosition( c1 );
			sumChange += Math.abs( c1.get().get() - r2.get().get() );
		}
		return sumChange;
	}
	
	public static <T> List<Block> generateBlocks(RandomAccessibleInterval<T> input, long blockSize, int sigma) {
		Config.setBlockSize(blockSize);
		final ExecutorService service = Threads.createExService( 1 );
		final long[] blockSizeDim = Util.getArrayFromValue( blockSize, input.numDimensions() );
		final BlockGenerator<Block> generator = new BlockGeneratorFixedSizePrecise( service, blockSizeDim );
		final double[] sigmas = Util.getArrayFromValue( (double)Config.getOverlap(), input.numDimensions() );
		final int[] halfKernelSizes = Gauss3.halfkernelsizes( sigmas );
		final long[] kernelSize = new long[ input.numDimensions() ];
		final long[] imgSize = new long[ input.numDimensions() ];
		for ( int d = 0; d < input.numDimensions(); ++d )
		{
			kernelSize[ d ] = halfKernelSizes[ d ] * 2 - 1;
			imgSize[ d ] = input.dimension( d );
		}
		final List< Block > blocks = generator.divideIntoBlocks( imgSize, kernelSize );
		return blocks;
	}
	
	public static ArrayList<BlockView> getBlocks(long[] sizes, int[] numBlocks, int blockSize, int sigma) {
		ArrayList<BlockView> blocks = new ArrayList<BlockView>();
		int lastBlockXSize = (int) ((sizes[0] % blockSize) > 0 ? sizes[0] % blockSize : blockSize);
		int lastBlockYSize = (int) ((sizes[1] % blockSize) > 0 ? (sizes[1] % blockSize) : blockSize);

		for (int i = 0; i < numBlocks[1]; i++) {
			for (int j = 0; j < numBlocks[0]; j++) {
				if (i < numBlocks[1]-1) {
					if (j < numBlocks[0]-1) {
						blocks.add(new BlockView(
								new Rectangle(j * blockSize, i * blockSize, blockSize + 2 * sigma,
										blockSize + 2 * sigma),
								new Rectangle(sigma + j * blockSize, sigma + i * blockSize, blockSize, blockSize),
								Colors.START));
					} else {
						blocks.add(new BlockView(
								new Rectangle(j * blockSize, i * blockSize, lastBlockXSize + 2 * sigma,
										blockSize + 2 * sigma),
								new Rectangle(sigma + j * blockSize, sigma + i * blockSize, lastBlockXSize, blockSize),
								Colors.START));
					}
				} else {
					if (j < numBlocks[0]-1) {
						blocks.add(new BlockView(
								new Rectangle(j * blockSize, i * blockSize, blockSize + 2 * sigma,
										lastBlockYSize + 2 * sigma),
								new Rectangle(sigma + j * blockSize, sigma + i * blockSize, blockSize, lastBlockYSize),
								Colors.START));
					} else {
						blocks.add(new BlockView(
								new Rectangle(j * blockSize, i * blockSize, lastBlockXSize + 2 * sigma,
										lastBlockYSize + 2 * sigma),
								new Rectangle(sigma + j * blockSize, sigma + i * blockSize, lastBlockXSize,
										lastBlockYSize),
								Colors.START));
					}
				}
			}
		}
		return blocks;
	}
}
