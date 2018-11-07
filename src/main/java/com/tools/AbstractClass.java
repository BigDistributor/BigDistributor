package main.java.com.tools;

import com.google.gson.Gson;

public class AbstractClass {
	public String test ;
	public  void fromJson(String jsonInString) {
		AbstractClass myclass  =  new Gson().fromJson(jsonInString, AbstractClass.class);
	}
	

}
