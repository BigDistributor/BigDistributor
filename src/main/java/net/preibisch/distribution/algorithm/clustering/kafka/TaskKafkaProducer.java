package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class TaskKafkaProducer extends TimerTask {
	private final KafkaProducer<String, String> producer;
	private KafkaMessage message;

	public TaskKafkaProducer(KafkaMessage message) {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty("acks", "1");
		props.setProperty("retries", "3");
		props.setProperty("linger.ms", "1");
		producer = new KafkaProducer<>(props);
		this.message = message;
	}

	@Override
	public void run() {
		producer.send(new ProducerRecord<>(message.getTopic(), message.getJobid(), message.toString()),
				new KafkaCallBack(message));
	}

	public static void main(String[] args) {
		TaskKafkaProducer producerThread = new TaskKafkaProducer(
				new KafkaMessage(KafkaTopics.TOPIC_DONE_TASK, "0", 1, "Hello"));
		Timer timer = new Timer();
		timer.schedule(producerThread, 1000);

	}
}
