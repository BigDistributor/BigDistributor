package main.java.net.preibisch.distribution.algorithm.clustering;

import java.io.File;

public class ClusterFile extends File{

	public ClusterFile(String parent, String child) {
		super(parent, child);
	}

	public String file(File f) {
		return new File(getPath(),f.getName()).getPath();
	}
}
