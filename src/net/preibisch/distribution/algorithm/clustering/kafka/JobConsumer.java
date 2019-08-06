package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import kafka.utils.ShutdownableThread;

public class JobConsumer extends ShutdownableThread {
    private final KafkaConsumer<String, String> consumer;
 
    public JobConsumer() {
    	 super("KafkaJobConsumer", false);

//    	 Logger logger = Logger.getLogger(this.getClass());
//     	logger.setLevel(Level.ERROR);
    	 Logger.getRootLogger().setLevel(Level.OFF);
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);

    }

    @Override
    public void doWork() {
    	Logger.getRootLogger().setLevel(Level.OFF);
    	
//        consumer.subscribe(Collections.singletonList(KafkaProperties.TOPIC_DONE_TASK));
        consumer.subscribe(KafkaTopics.getTopics());
        while(true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
        	KafkaMessageManager.process(record);

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
    	  JobConsumer consumerThread = new JobConsumer();
          consumerThread.start();

    }
}