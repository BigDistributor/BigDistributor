package net.preibisch.bigdistributor.algorithm.clustering.kafka;

import java.util.Timer;

public class KafkaManager {
	public String jobID;

	public KafkaManager(String jobID) {
		this.jobID = jobID;
	}

	public void log(int blockid, String log) {
		KafkaMessage message = new KafkaMessage(KafkaTopics.TOPIC_LOG_TASK, jobID, blockid, log);
		send(message);
	}

	public void error(int blockid, String log) {
		KafkaMessage message = new KafkaMessage(KafkaTopics.TOPIC_ERROR_TASK, jobID, blockid, log);
		send(message);
	}

	public void done(int blockid, String log) {
		KafkaMessage message = new KafkaMessage(KafkaTopics.TOPIC_DONE_TASK, jobID, blockid, log);
		send(message);
	}

	public void send(KafkaMessage message) {
		TaskKafkaProducer producer = new TaskKafkaProducer(message);
		Timer timer = new Timer();
		timer.schedule(producer, 1000);
	}

	public void test(KafkaMessage msg) {
		TaskKafkaProducer producerThread = new TaskKafkaProducer(msg);
		Timer timer = new Timer();
		timer.schedule(producerThread, 1000);
	}

	public static void main(String[] args) {
//		KafkaMessage msg = new KafkaMessage(KafkaTopics.TOPIC_DONE_TASK, "0", 1, "Hello");
		KafkaManager manager = new KafkaManager("00");
//		manager.log(0, "Start process");
		manager.done(0, "Start process");
	}

}
