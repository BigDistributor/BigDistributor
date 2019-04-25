package main.java.net.preibisch.distribution.tools.config;

public class KafkaProperties {
    public static final String TOPIC_DONE_TASK = "TASK_DONE";
    public static String KAFKA_SERVER_URL = "141.80.219.131";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String CLIENT_ID = "mzouink";
    
	public static void setURL(String text) {
		KAFKA_SERVER_URL = text;		
	}
}
