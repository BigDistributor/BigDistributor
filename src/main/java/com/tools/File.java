package main.java.com.tools;

public class File {
	final private String path;
	final private String name;
	final private Extension extension;

	private File(Builder builder) {
		this.path = builder.path;
		this.name = builder.name;
		this.extension = builder.extension;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public Extension getExtension() {
		return extension;
	}

	public static class Builder {
		private String path;
		private String name;
		private Extension extension;

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder extension(Extension extenstion) {
			this.extension = extenstion;
			return this;
		}

		public File build() {
			return new File(this);
		}
	}
}
