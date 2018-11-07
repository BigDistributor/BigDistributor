package main.java.com.tools;

import com.google.gson.Gson;

public class AbstractBuilder {
	String test ="ghello";
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public static void main(String[] args) {
		AbstractBuilder tester = new AbstractBuilder();
		System.out.println(tester.toJson());
	}
}
