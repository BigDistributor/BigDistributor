package clustering.kafka;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class TaskDoneProducer extends TimerTask {
    private final KafkaProducer<String, String> producer;
    private final String topic;
	private String msg;

    public TaskDoneProducer(String topic, String msg) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.msg = msg;
    }

    public void run() {
            long startTime = System.currentTimeMillis();
            String messageStr = startTime+";"+msg;
                producer.send(new ProducerRecord<>(topic,
                    msg,
                    messageStr), new KafkaCallBack(startTime, msg, messageStr));
    }
    
    public static void main(String[] args) {
        
    	TaskDoneProducer producerThread = new TaskDoneProducer(KafkaProperties.TOPIC_DONE_TASK,String.join(";", args));
        Timer timer = new Timer();
        timer.schedule(producerThread, 1000);

	}
}


