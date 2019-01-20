package main.java.net.preibisch.distribution.algorithm.controllers.items;

public class Job extends Object{
	final private JFile task;
	final private JFile extra;
	final private JDataFile input;
	final private AppMode appMode;

	private Job(Builder builder) {
		this.task = builder.task;
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

	public static class Builder {
		private JFile task;
		private JFile extra;
		private JDataFile input;
		private AppMode appMode;

		public Builder task(JFile task) {
			this.task = task;
			return this;
		}

		public Builder input(JDataFile input) {
			this.input = input;
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
