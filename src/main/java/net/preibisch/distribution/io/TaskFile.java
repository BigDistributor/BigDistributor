package net.preibisch.distribution.io;

import java.io.IOException;

import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;

public class TaskFile extends DistributedFile{
	
	private TaskType type = TaskType.FUSION ;

	public TaskFile(String path,TaskType type, FileStatus mode) throws IOException {
		super(path,mode);
		this.type = type;
	}

}
