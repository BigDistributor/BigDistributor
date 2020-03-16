package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import kafka.utils.ShutdownableThread;
import net.preibisch.distribution.algorithm.errorhandler.logmanager.MyLogger;

public class JobConsumer extends ShutdownableThread {
	private final KafkaConsumer<String, String> consumer;

	private static Consumer<String, String> createConsumer() {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.getUrls());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		final Consumer<String, String> consumer = new KafkaConsumer<>(props);

		consumer.subscribe(KafkaTopics.getTopics());
		// consumer.subscribe(Collections.singletonList(KafkaTopics.TOPIC_DONE_TASK));
		return consumer;
	}

	public JobConsumer() {
		super("KafkaJobConsumer", false);
		// Logger logger = Logger.getLogger(this.getClass());
		// logger.setLevel(Level.ERROR);
		Logger.getRootLogger().setLevel(Level.OFF);
		Properties props = new Properties();
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "false");
		// earliest
		props.setProperty("auto.offset.reset", "latest");
		props.setProperty("key.deserializer", StringDeserializer.class.getName());
		props.setProperty("value.deserializer", StringDeserializer.class.getName());
		consumer = new KafkaConsumer<>(props);
	}

	@Override
	public void doWork() {
		Logger.getRootLogger().setLevel(Level.OFF);

		consumer.subscribe(KafkaTopics.getTopics());
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10000);
			for (ConsumerRecord<String, String> record : records) {
				KafkaLogManager.process(record);
			}
		}
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	public static void main(String[] args) {
		MyLogger.log().info("Start collect log");
		JobConsumer consumerThread = new JobConsumer();
		consumerThread.start();
	}
}