package net.preibisch.distribution.algorithm.task;

import mpicbg.spim.data.SpimDataException;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;

public interface Params {

	public Params fromJson(String path) throws Exception;
	public void toJson(String path);
	
	public SpimData2 getSpimData() throws SpimDataException;
	
}
