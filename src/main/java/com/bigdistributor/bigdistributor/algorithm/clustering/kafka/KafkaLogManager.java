package net.preibisch.bigdistributor.algorithm.clustering.kafka;

import net.preibisch.bigdistributor.algorithm.errorhandler.logmanager.MyLogger;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaLogManager {
	// TO be developed to move message to logger
	public static void process(ConsumerRecord<String, String> record) {
		MyLogger.log().info("Received message: (" + record.topic() + "," + record.key() + ", " + record.value()
				+ ") at offset " + record.offset());
	}

}
