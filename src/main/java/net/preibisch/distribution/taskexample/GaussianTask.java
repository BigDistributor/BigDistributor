package net.preibisch.distribution.taskexample;

import java.util.concurrent.ExecutorService;

import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.distribution.algorithm.AbstractTask2;
import net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import net.preibisch.distribution.io.img.n5.tests.LoadN5;
import net.preibisch.distribution.tools.Threads;

public class  GaussianTask implements AbstractTask2< RandomAccessibleInterval<FloatType>,  RandomAccessibleInterval<FloatType>, Integer> {

	
	public static void main(String[] args) throws Exception {
		
		String[] ar = testArgs();
//		CommandLine.call(new MainJob(new GaussianTask()), ar);
		
		show();
	}

	private static void show() {
		String in = "/home/mzouink/Desktop/test/in.n5";

		String out = "/home/mzouink/Desktop/test/out.n5";
		
		new ImageJ();
		ImageJFunctions.show(new LoadN5(in).fuse(),"in");

		ImageJFunctions.show(new LoadN5(out).fuse(),"out");
		
	}

	private static String[] testArgs() throws Exception {
		String in = "/home/mzouink/Desktop/test/in.n5";

		String out = "/home/mzouink/Desktop/test/in.n5";
		String meta = "/home/mzouink/Desktop/test/METADATA.json";;
		int id = 4;
		String args = "-in "+in+" -out "+out+" -m "+meta+" -id "+id;

		System.out.println(args);
		return args.split(" ");
	}

	@Override
	public RandomAccessibleInterval<FloatType> start(RandomAccessibleInterval<FloatType> input, Integer params,
			AbstractCallBack callback) throws Exception {
		final ExecutorService service = Threads.createExService(1);
		final double[] sigmas = Util.getArrayFromValue(30.0, input.numDimensions());
		Gauss3.gauss(sigmas, Views.extendMirrorSingle(input), input, service);
		return input;
	}


}
