package main.java.net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import kafka.utils.ShutdownableThread;
import main.java.net.preibisch.distribution.algorithm.controllers.items.callback.AbstractCallBack;
import main.java.net.preibisch.distribution.tools.config.KafkaProperties;

public class JobConsumer extends ShutdownableThread {
    private final KafkaConsumer<Integer, String> consumer;
 
    private AbstractCallBack callback;

    public JobConsumer(AbstractCallBack callback) {
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
         this.callback = callback;
        callback.log("Listner created");
    }

    @Override
    public void doWork() {
    	Logger.getRootLogger().setLevel(Level.OFF);
        consumer.subscribe(Collections.singletonList(KafkaProperties.TOPIC_DONE_TASK));
        while(true) {
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        for (ConsumerRecord<Integer, String> record : records) {
        	System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());

            callback.log(record.value());
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
        JobConsumer consumerThread = new JobConsumer(new AbstractCallBack() {
			
			@Override
			public void onSuccess(int pos) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void log(String log) {
				System.out.println("test log: "+log);
				
			}
		});
        consumerThread.start();

    }
}