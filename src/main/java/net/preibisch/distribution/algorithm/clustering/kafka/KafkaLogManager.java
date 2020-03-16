package net.preibisch.distribution.algorithm.clustering.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;

public class KafkaLogManager {
	// TO be developed to move message to logger
	public static void process(ConsumerRecord<String, String> record) {
		MyLogger.log().info("Received message: (" + record.topic() + "," + record.key() + ", " + record.value()
				+ ") at offset " + record.offset());
	}

}
