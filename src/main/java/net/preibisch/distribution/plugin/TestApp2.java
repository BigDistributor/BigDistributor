package main.java.net.preibisch.distribution.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.GzipCompression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import ij.ImageJ;
import main.java.net.preibisch.distribution.algorithm.blockmanager.BlockInfos;
import main.java.net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import main.java.net.preibisch.distribution.algorithm.controllers.items.MetaDataGenerator;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.Callback;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.taskexample.Fusion;
import main.java.net.preibisch.distribution.tools.Tools;
import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.fusion.FusionTools;

public class TestApp2 {
	public static void main(String[] args) throws IOException, SpimDataException {

		new ImageJ();
		MyLogger.initLogger();
		final String tmpDir = "/home/mzouink/Desktop/testsave/";
		final String input_path = "/home/mzouink/Desktop/testn5/dataset.xml";
		final String output_path = "/home/mzouink/Desktop/testn5/realdata.n5";

//		Tools.cleanFolder(tmpDir);

//		SpimData2 spimData = SpimData2.convert(SimulatedBeadsImgLoader.spimdataExample(new int[] { 0, 0, 0 }));
		SpimData2 spimData = new XmlIoSpimData2( "" ).load(input_path);
		
		for(BoundingBox box : spimData.getBoundingBoxes().getBoundingBoxes()) {
			System.out.println(box.toString());
		}

		final List<ViewId> viewIds = new ArrayList<ViewId>();
		viewIds.addAll(spimData.getSequenceDescription().getViewDescriptions().values());

		// small part of the bounding box\
		Interval bb = new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 980, 1428, 392 });

		// downsampling
		double downsampling = Double.NaN; // 2.0
//		double downsampling = 2; // 2.0

		// perforn the fusion virtually
		RandomAccessibleInterval<FloatType> source = FusionTools.fuseVirtual(spimData, viewIds, bb, downsampling);

		ImageJFunctions.show(source, "Input");
		
		long[] dims = Tools.dimensions(source);
		System.out.println("Dims: " + Util.printCoordinates(dims));

		int[] blockSize  = new int[dims.length];
		Arrays.fill(blockSize , 80);
		System.out.println("Blocks: " + Util.printCoordinates(blockSize));
		
		BlocksMetaData md = MetaDataGenerator.genarateMetaData(dims, Util.int2long(blockSize), 0, new Callback());

		int total = md.getBlocksInfo().size();
		System.out.println(md.toString());
	
		N5Writer n5 = new N5FSWriter(output_path);
		
		final DatasetAttributes attributes = new DatasetAttributes(
				dims,
				blockSize,
				N5Utils.dataType(Util.getTypeFromInterval(source)),
				new GzipCompression());

		String dataset = "/volumes/raw" ;
		n5.createDataset(dataset , attributes);
		System.out.println("dataset created : "+ output_path);
		
		RandomAccessibleInterval<FloatType> virtual = N5Utils.open(new N5FSReader(output_path), dataset);
		ImageJFunctions.show(virtual,"Black output");

		ExecutorService executor = Executors.newFixedThreadPool(10);

		for(int i =0; i< total; i++) {
			Runnable myTask = new Task(i,input_path,n5,dataset,output_path,md.getBlocksInfo().get(i));
			executor.submit(myTask);
		}
		
//		RandomAccessibleInterval<FloatType> virtual2 = N5Utils.open(new N5FSReader(output_path), dataset);
//		ImageJFunctions.show(virtual2," output");

	}	
}


class Task implements Runnable{
	private int id;
	private String source_xml;
	private N5Writer n5;
	private String dataset;
	private String output_path;
	private BlockInfos binfo;
	

	public Task(int i, String source_xml, N5Writer n5, String dataset, String output_path,
			BlockInfos binfo) {
		this.source_xml = source_xml;
		this.id = i;
		this.n5 = n5;
		this.dataset = dataset;
		this.output_path = output_path;
		this.binfo = binfo;
	}
	@Override
	public void run() {
		
			RandomAccessibleInterval<FloatType> block;
			try {
				block = Fusion.Fusion(source_xml, binfo.getX1(),binfo.getX2());
				N5Utils.saveBlock(block, n5, dataset,  binfo.getGridOffset());
				MyLogger.log.info("Block "+id+" saved !");
			} catch (SpimDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		
	}
	
}

