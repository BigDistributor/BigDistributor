package net.preibisch.distribution.algorithm.clustering;

import java.io.File;

public class ClusterFile extends File{

	public ClusterFile(String parent, String child) {
		super(parent, child);
	}

	public File subfile(File f) {
		return new File(getPath(),f.getName());
	}
	public File subfile(String s) {
		return new File(getPath(),s);
	}
}
