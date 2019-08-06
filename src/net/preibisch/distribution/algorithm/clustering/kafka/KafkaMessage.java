package net.preibisch.distribution.algorithm.clustering.kafka;

public class KafkaMessage {
	private int ts;
	private int block;
	private String msg;
	
	public KafkaMessage(String msg) {
		String[] parts = msg.split(";");
		ts = Integer.valueOf(parts[0]);
		block = Integer.valueOf(parts[1]);
		msg = parts[2];
	}
	
	public int getTs() {
		return ts;
	}
	
	public int getBlock() {
		return block;
	}
	
	public String getMsg() {
		return msg;
	}
	
	@Override
	public String toString() {
		return "ts: "+ ts+ " |block: "+block+" |msg:"+msg;
	}
}
