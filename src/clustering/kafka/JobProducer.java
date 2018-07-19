package clustering.kafka;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import clustering.CheckJobStatus;

public class JobProducer extends TimerTask {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
	private int messageNo;

    public JobProducer(String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
        this.topic = topic;
        messageNo = 1;
    }

    public void run() {
            long startTime = System.currentTimeMillis();
            String messageStr = startTime+";"+CheckJobStatus.countRunningJobs();
                producer.send(new ProducerRecord<>(topic,
                    messageNo,
                    messageStr), new KafkaCallBack(startTime, messageNo, messageStr));
            ++messageNo;
    }
    
    public static void main(String[] args) {
        
    	JobProducer producerThread = new JobProducer(KafkaProperties.TOPIC);
        Timer timer = new Timer();
        timer.schedule(producerThread, 0, 10000);
	}
}
