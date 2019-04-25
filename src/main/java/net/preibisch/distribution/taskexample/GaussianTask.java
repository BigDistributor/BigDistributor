package main.java.net.preibisch.distribution.taskexample;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import main.java.net.preibisch.distribution.algorithm.AbstractTask2;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.plugin.MainJob;
import main.java.net.preibisch.distribution.tools.Threads;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import picocli.CommandLine;

public class  GaussianTask implements AbstractTask2< RandomAccessibleInterval<FloatType>,  RandomAccessibleInterval<FloatType>, String,Integer> {

	@Override
	public void start(RandomAccessibleInterval<FloatType> input, RandomAccessibleInterval<FloatType> output,
			Map<String, Integer> params, AbstractCallBack callback) throws IncompatibleTypeException {
			final ExecutorService service = Threads.createExService(1);
			final double[] sigmas = Util.getArrayFromValue(8.0, input.numDimensions());
			Gauss3.gauss(sigmas, Views.extendMirrorSingle(input), output, service);
	}
	
	public static void main(String[] args) {
		CommandLine.call(new MainJob(new GaussianTask()), args);
	}


}
