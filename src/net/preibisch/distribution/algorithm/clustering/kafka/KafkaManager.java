package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Timer;

public class KafkaManager {
	public static void log(int block, String msg) {
		send(KafkaTopics.LOG, block, msg);
	}

	public static void error(int block, String msg) {
		send(KafkaTopics.ERROR, block, msg);
	}

	public static void done(int block, String msg) {
		send(KafkaTopics.DONE, block, msg);
	}

	private static void send(KafkaTopics type, int block, String msg) {
		String id =  KafkaProperties.getJobId();
		String message =  block + ";" + msg;
		String topic = KafkaTopics.getTopic(type);
		TaskDoneProducer producerThread = new TaskDoneProducer(topic,id, message);
		new Timer().schedule(producerThread, 1000);
	}

}
