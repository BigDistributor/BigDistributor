package net.preibisch.distribution.algorithm.controllers.items;

import com.google.gson.Gson;

public class AbstractBuilder {
	public String test ="hello";
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public static void main(String[] args) {
		AbstractBuilder tester = new AbstractBuilder();
		System.out.println(tester.toJson());
	}
}
