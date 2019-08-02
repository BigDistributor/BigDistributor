package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Timer;

public class KafkaManager {
	public static void log(int block, String msg) {
		send(KafkaMessage.LOG, block, msg);
	}

	public static void error(int block, String msg) {
		send(KafkaMessage.ERROR, block, msg);
	}

	public static void done(int block, String msg) {
		send(KafkaMessage.DONE, block, msg);
	}

	private static void send(KafkaMessage type, int block, String msg) {
		String message = KafkaProperties.getJobId()+ ";" + block + ";" + msg;
		String topic = KafkaMessage.getTopic(type);
		TaskDoneProducer producerThread = new TaskDoneProducer(topic, message);
		new Timer().schedule(producerThread, 1000);
	}

}
