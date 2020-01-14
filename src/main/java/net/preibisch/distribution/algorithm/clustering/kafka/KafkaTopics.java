package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Arrays;
import java.util.Collection;

enum KafkaTopics {
	ERROR, DONE, LOG;

	public static final String TOPIC_DONE_TASK = "TASKDONE";
	public static final String TOPIC_ERROR_TASK = "TASKERROR";
	public static final String TOPIC_LOG_TASK = "TASKLOG";

	public static String getTopic(KafkaTopics msg) {
		switch (msg) {
		case LOG:
			return TOPIC_LOG_TASK;
		case ERROR:
			return TOPIC_ERROR_TASK;
		case DONE:
			return TOPIC_DONE_TASK;
		default:
			return TOPIC_LOG_TASK;
		}
	}

	public static KafkaTopics of(String msg) {
		switch (msg) {
		case TOPIC_LOG_TASK:
			return LOG;
		case TOPIC_ERROR_TASK:
			return ERROR;
		case TOPIC_DONE_TASK:
			return DONE;
		default:
			return LOG;
		}
	}

	public static Collection<String> getTopics() {
		Collection<String> list = Arrays.asList(TOPIC_DONE_TASK, TOPIC_ERROR_TASK, TOPIC_LOG_TASK);
		return list;
	}
}
