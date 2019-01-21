package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.io.File;

import com.google.common.io.Files;

public class Job extends Object{
	final private JFile task;
	final private JFile extra;
	final private JDataFile input;
	final private AppMode appMode;
	final private String tmpDir;

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
		
		public Builder createTempDir() {
			File tempDir = Files.createTempDir();
			tmpDir = tempDir.getAbsolutePath();
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

}
