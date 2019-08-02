package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Arrays;
import java.util.Collection;

enum KafkaMessage {
	ERROR, DONE, LOG;

	public static String getTopic(KafkaMessage msg) {
		switch (msg) {
		case LOG:
			return KafkaProperties.TOPIC_LOG_TASK;
		case ERROR:
			return KafkaProperties.TOPIC_ERROR_TASK;
		case DONE:
			return KafkaProperties.TOPIC_DONE_TASK;
		default:
			return KafkaProperties.TOPIC_LOG_TASK;
		}
	}
}

public class KafkaProperties {
	public static final String TOPIC_DONE_TASK = "TASK_DONE";
	public static final String TOPIC_ERROR_TASK = "TASK_ERROR";
	public static final String TOPIC_LOG_TASK = "TASK_LOG";
	public static String KAFKA_SERVER_URL = "141.80.219.131";
	public static final int KAFKA_SERVER_PORT = 9092;
	public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
	public static final int CONNECTION_TIMEOUT = 100000;
	public static final String CLIENT_ID = "mzouink";
	public static  String jobId = "0";

	public static void setURL(String url) {
		KAFKA_SERVER_URL = url;
	}
	
	public static void setJobId(String jobId) {
		KafkaProperties.jobId = jobId;
	}
	
	public static String getJobId() {
		return jobId;
	}
	
	public static Collection<String> getTopics(){
		Collection<String> list = Arrays.asList(TOPIC_DONE_TASK,TOPIC_ERROR_TASK,TOPIC_LOG_TASK);
		return list;
	}
}
