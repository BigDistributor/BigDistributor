package net.preibisch.distribution.algorithm.clustering.kafka;

import java.util.Timer;

import org.scijava.log.LogListener;
import org.scijava.log.LogMessage;
import org.scijava.log.LogSource;
import org.scijava.log.Logger;

// TODO implement Logger and then automatically load from MyLogger
public class KafkaManager implements Logger{
	public static String jobID;
	public static void log( int block, String msg) {
		send(KafkaTopics.LOG, block, msg);
	}

	public static void error(int block, String msg) {
		send(KafkaTopics.ERROR, block, msg);
	}

	public static void done(int block, String msg) {
		send(KafkaTopics.DONE, block, msg);
	}

	private static void send(KafkaTopics type, int block, String msg) {
		String id =  KafkaProperties.getJobId();
		String message =  block + ";" + msg;
		String topic = KafkaTopics.getTopic(type);
		TaskDoneProducer producerThread = new TaskDoneProducer(topic,id, message);
		new Timer().schedule(producerThread, 1000);
	}

	@Override
	public void alwaysLog(int level, Object msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LogSource getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger subLogger(String name, int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLogListener(LogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLogListener(LogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyListeners(LogMessage message) {
		// TODO Auto-generated method stub
		
	}

}
