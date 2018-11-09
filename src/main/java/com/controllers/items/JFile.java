package main.java.com.controllers.items;

import java.util.Arrays;


public class JFile {
	final private String path;
	final private String name;
	final private Extension extension;
	final private String all;

	protected JFile(Builder builder) {
		this.path = builder.path;
		this.name = builder.name;
		this.extension = builder.extension;
		this.all = builder.all;
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
	
	public String getAll() {
		return all;
	}

	public static class Builder {
		private String path;
		private String name;
		private Extension extension;
		protected String all;

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

		public Builder fromString(String string) {
			String[] parts = string.split("/");
			String lastElement = parts[parts.length-1];
			
			this.all = string;
			this.path = String.valueOf(Arrays.copyOfRange(parts, 0, parts.length-2));
			this.name = lastElement.split(".")[0];
			this.extension = Extension.formString(lastElement.split(".")[1]);
			return this;
		}

		public JFile build() {
			return new JFile(this);
		}

	}
}
