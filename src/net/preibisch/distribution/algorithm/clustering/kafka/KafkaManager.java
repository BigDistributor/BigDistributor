package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Timer;

import net.preibisch.distribution.algorithm.controllers.items.Job;

public class KafkaManager {
	public static void log(String msg) {
		send(KafkaMessage.LOG, msg);
	}

	public static void error(String msg) {
		send(KafkaMessage.ERROR, msg);
	}

	public static void done(String msg) {
		send(KafkaMessage.DONE, msg);
	}

	private static void send(KafkaMessage type, String msg) {
		String message = Job.getId() + ";" + msg;
		String topic = KafkaMessage.getTopic(type);
		TaskDoneProducer producerThread = new TaskDoneProducer(topic, message);
		new Timer().schedule(producerThread, 1000);
	}

}
