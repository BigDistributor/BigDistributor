package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import main.java.net.preibisch.distribution.algorithm.AbstractTask2;
import main.java.net.preibisch.distribution.io.img.ImgFile;

public class Job<T, I, P> extends Object{

	public static final String TASK_CLUSTER_NAME = "task.jar";
	
	final private AbstractTask2<T, I, P> task;
	final private AbstractTaskParams extra;
	
	final private ImgFile input;

	final private ImgFile output;
	final private AppMode appMode;
	final private String tmpDir;

	private String metaDataPath;

	private String taskShellPath;
	
	public static Job initJob(AppMode mode, String task, String input) {
		JDataFile inputData = new JDataFile.Builder()
				.file(JFile.of(input))
				.load()
				.getDataInfos()
				.build();
		
		return new Job.Builder()
			     .appMode(mode)
			     .task(JFile.of(task))
//			     .extra(JFile.of(EXTRA_FILE_PATH))
			     .input(inputData)
			     .createTempDir()
			     .buid();
	}
	
	public static Job initJob(AppMode mode, String task, String input, String tmpDir) {
		JDataFile inputData = new JDataFile.Builder()
				.file(JFile.of(input))
				.load()
				.getDataInfos()
				.build();
		
		return new Job.Builder()
			     .appMode(mode)
			     .task(JFile.of(task))
//			     .extra(JFile.of(EXTRA_FILE_PATH))
			     .input(inputData)
			     .tmp(tmpDir)
			     .buid();
	}

	private Job(Builder builder) {
		this.task = builder.task;
		this.tmpDir = builder.tmpDir;
		this.input = builder.input;
		this.appMode = builder.appMode;
		this.extra = builder.extra;
	}

	public JFile getTask() {
		return task;
	}

	public JDataFile getInput() {
		return input;
	}
	
	public JFile getExtra() {
		return extra;
	}

	public AppMode getAppMode() {
		return appMode;
	}
	
	public String getTmpDir() {
		return tmpDir;
	}
	
	public String getMetaDataPath() {
		return metaDataPath;
	}

	public void setMetaDataPath(String metaDataPath) {
		this.metaDataPath = metaDataPath;
	}
	
	public void setTaskShellPath(String taskShellPath) {
		this.taskShellPath = taskShellPath;
		
	}
	
	public String getTaskShellPath() {
		return taskShellPath;
	}


	public static class Builder {
		private JFile task;
		private JFile extra;
		private JDataFile input;
		private String tmpDir;
		private AppMode appMode;

		public Builder task(JFile task) {
			this.task = task;
			return this;
		}

		public Builder input(JDataFile input) {
			this.input = input;
			return this;
		}
		
		public Builder tmp(String dir) {
			this.tmpDir = dir;
			return this;
		}
		
		public Builder createTempDir() {
			File tempDir = Files.createTempDir();
			this.tmpDir = tempDir.getAbsolutePath();
			System.out.println("TempFolder: "+tmpDir);
			return this;
		}
		
		public Builder extra(JFile extra) {
			this.extra = extra;
			return this;
		}

		public Builder appMode(AppMode appMode) {
			this.appMode = appMode;
			return this;
		}

		public Job buid() {
			return new Job(this);
		}
	}


	public void openTempFolder()  {
		try {
			Runtime.getRuntime().exec("open "+tmpDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
