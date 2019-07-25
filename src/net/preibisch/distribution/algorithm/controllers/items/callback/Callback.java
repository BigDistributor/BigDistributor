package net.preibisch.distribution.algorithm.controllers.items.callback;

public class Callback implements AbstractCallBack {

	@Override
	public void onSuccess(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String error) {
		System.out.println("Error!: "+error);
		
	}

	@Override
	public void log(String log) {
		System.out.println("Info: "+log);
		
	}

}
