package clustering.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class TaskDoneProducer {
    private final KafkaProducer<Integer, Integer> producer;

    public TaskDoneProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void send(int taskID) {
    	long startTime = System.currentTimeMillis();
        producer.send(new ProducerRecord<>(KafkaProperties.TOPIC_DONE_TASK,taskID,taskID), new KafkaCallBack(startTime, taskID, String.valueOf(taskID)));
    }

    public static void main(String[] args) {
        
    	TaskDoneProducer prod = new TaskDoneProducer();
    	prod.send(Integer.parseInt(args[0]));
	}
}
