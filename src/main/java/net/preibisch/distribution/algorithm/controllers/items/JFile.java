package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.util.Arrays;


public class JFile extends Object{
	final private String path;
	final private String name;
	final private JExtension extension;
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

	public JExtension getExtension() {
		return extension;
	}
	
	public String getAll() {
		return all;
	}
	
	
	@Override
	public String toString() {
		return "JFile [path=" + path + ", name=" + name + ", extension=" + extension + "]";
	}


	public static class Builder {
		private String path;
		private String name;
		private JExtension extension;
		protected String all;

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder extension(JExtension extenstion) {
			this.extension = extenstion;
			return this;
		}
		
		public Builder all(String all) {
			this.all = all;
			return this;
		}

		public Builder fromString(String string) {
			System.out.println("File: "+string);
			String[] parts = string.split("/");
			String lastElement = parts[parts.length-1];
			System.out.println("LastElm: "+lastElement);

			this.all = string;
			this.path = String.valueOf(Arrays.copyOfRange(parts, 0, parts.length-2));
			String[] elements = lastElement.split(".");
			for (int i = 0; i < elements.length; i++) {
				System.out.println(i+"-"+elements[i]);
			}
			this.name = lastElement.split(".")[0];

			System.out.println("LastElm: "+lastElement.split(".").toString());
			this.extension = JExtension.fromString(lastElement.split(".")[1]);
			return this;
		}

		public JFile build() {
			return new JFile(this);
		}

	}
	public static void main(String[] args) {
		JFile file = new JFile.Builder()
				.all("test")
				.path("testPath")
				.name("name")
				.extension(JExtension.fromString("tif"))
				.build();
		
		System.out.println(file);
	}
}
