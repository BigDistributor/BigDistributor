package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class TaskDoneProducer extends TimerTask {
    private final KafkaProducer<String, String> producer;
    private String topic;
    private String id;
	private String msg;

    public TaskDoneProducer(String topic, String id, String msg) {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty("acks", "1");
        props.setProperty("retries", "3");
        props.setProperty("linger.ms", "1");
        producer = new KafkaProducer<>(props);
        this.id = id;
        this.topic = topic;
        this.msg = msg;
    }

    @Override
	public void run() {
            long startTime = System.currentTimeMillis();
            String messageStr = startTime+";"+msg;
                producer.send(new ProducerRecord<>(topic,
                    id,
                    messageStr), new KafkaCallBack(startTime, msg, messageStr));
    }
    
    public static void main(String[] args) {
        
    	TaskDoneProducer producerThread = new TaskDoneProducer(KafkaTopics.TOPIC_DONE_TASK,"0",String.join(";", args));
        Timer timer = new Timer();
        timer.schedule(producerThread, 1000);

	}
}


