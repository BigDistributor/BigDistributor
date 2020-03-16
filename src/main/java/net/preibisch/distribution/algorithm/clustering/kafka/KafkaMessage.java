package net.preibisch.distribution.algorithm.clustering.kafka;

public class KafkaMessage {
    private String topic;
    private String jobid;
    private long ts;
	private int blockid;
	private String log;

	private KafkaMessage(String topic, String jobid, long ts, int blockid, String log) {
		super();
		this.topic = topic;
		this.jobid = jobid;
		this.ts = ts;
		this.blockid = blockid;
		this.log = log;
	}
	
	public KafkaMessage(String topic, String jobid, int blockid, String log) {
		super();
		this.topic = topic;
		this.jobid = jobid;
		this.ts = System.currentTimeMillis();
		this.blockid = blockid;
		this.log = log;
	}

	public KafkaMessage(String topic, String jobid,String message) {
		this.topic = topic;
		this.jobid = jobid;
		String[] parts = message.split(";");
		ts = Long.valueOf(parts[0]);
		blockid = Integer.valueOf(parts[1]);
		log = parts[2];
	}

	public long getTimestamp() {
		return ts;
	}
	
	public String getTopic() {
		return topic;
	}
	public String getJobid() {
		return jobid;
	}

	@Override
	public String toString() {
		return String.format("%d;%d;%s",ts, blockid,log);
	}

	public static void main(String[] args) {
		KafkaMessage message = new KafkaMessage(KafkaTopics.TOPIC_DONE_TASK,"0","1565260449612;9;Task finished 9");
		System.out.println(message);
	}
}
