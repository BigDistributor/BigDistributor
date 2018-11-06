package main.java.com.tools;

public class Job {
	final private File task;
	final private File input;

	public Job(Builder builder) {
		this.task = builder.task;
		this.input = builder.input;
	}

	public File getTask() {
		return task;
	}

	public File getInput() {
		return input;
	}

	public static class Builder {
		private File task;
		private File input;

		public Builder task(File task) {
			this.task = task;
			return this;
		}

		public Builder input(File input) {
			this.input = input;
			return this;
		}

		public Job buid() {
			return new Job(this);
		}
	}

}
