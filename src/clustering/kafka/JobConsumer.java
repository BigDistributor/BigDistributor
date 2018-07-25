package clustering.kafka;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import clustering.MyCallBack;

import java.util.Collections;
import java.util.Properties;

public class JobConsumer extends ShutdownableThread {
    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;
    private MyCallBack callback;

    public JobConsumer(String topic, MyCallBack callback) {
        super("KafkaConsumerExample", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
        this.callback = callback;
        callback.log("Listner created");
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(this.topic));
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        for (ConsumerRecord<Integer, String> record : records) {
            callback.log("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
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
        JobConsumer consumerThread = new JobConsumer(KafkaProperties.TOPIC_DONE_TASK,new MyCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void log(String log) {
				// TODO Auto-generated method stub
				
			}
		});
        consumerThread.start();

    }
}