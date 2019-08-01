package net.preibisch.distribution.algorithm.clustering.kafka;

public class KafkaProperties {
    public static final String TOPIC_DONE_TASK = "TASK_DONE";
    public static final String TOPIC_ERROR_TASK = "TASK_ERROR";
    public static final String TOPIC_LOG_TASK = "TASK_LOG";
    public static String KAFKA_SERVER_URL = "141.80.219.131";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String CLIENT_ID = "mzouink";
    
    
    public static void setURL(String url){
    	KAFKA_SERVER_URL = url;
    }
}
