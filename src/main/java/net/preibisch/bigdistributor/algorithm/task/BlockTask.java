package net.preibisch.bigdistributor.algorithm.task;

public interface BlockTask<T> {
	void blockTask(String inputPath, String metadataPath, String paramPath, String outputPath, Integer blockID) ;
}
