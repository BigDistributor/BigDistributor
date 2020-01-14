package net.preibisch.distribution.algorithm.clustering.kafka;

public class KafkaMessage {
	private long ts;
	private int block;
	private String msg;

	public KafkaMessage(String message) {
		String[] parts = message.split(";");
		ts = Long.valueOf(parts[0]);
		block = Integer.valueOf(parts[1]);
		msg = parts[2];
	}

	public long getTs() {
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
		return "ts: " + ts + " |block: " + block + " |msg:" + msg;
	}

	public static void main(String[] args) {
		KafkaMessage message = new KafkaMessage("1565260449612;9;Task finished 9");
		System.out.println(message);
	}
}
