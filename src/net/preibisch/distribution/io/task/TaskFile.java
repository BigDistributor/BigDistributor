package net.preibisch.distribution.io.task;

import java.io.File;

import net.preibisch.distribution.algorithm.controllers.items.DataExtension;

public class TaskFile extends File {

	protected DataExtension extension;

	public TaskFile(String path) {
		super(path);
		this.extension = DataExtension.fromURI(path);

	}

	public DataExtension getExtension() {
		return extension;
	}

}
