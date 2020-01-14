package net.preibisch.distribution.algorithm.controllers.items;

import com.google.gson.Gson;

public class AbstractClass {
	public String test ;
	public  void fromJson(String jsonInString) {
		AbstractClass myclass  =  new Gson().fromJson(jsonInString, AbstractClass.class);
	}
	

}
