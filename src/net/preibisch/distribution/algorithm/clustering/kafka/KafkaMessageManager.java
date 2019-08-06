package net.preibisch.distribution.algorithm.clustering.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import mpicbg.spim.io.IOFunctions;
import net.preibisch.distribution.gui.items.Colors;
import net.preibisch.distribution.gui.items.DataPreview;

public class KafkaMessageManager {
	

	private static String jobId;

	public KafkaMessageManager(String id) {
		KafkaMessageManager.jobId = id;
			JobConsumer consumerThread = new JobConsumer();
			consumerThread.start();
	}
	
	  public static void process(ConsumerRecord<String, String> record) {
			if(jobId==record.key()) {
	        	System.out.println("Received message: (" + record.topic()+","+record.key() + ", " + record.value() + ") at offset " + record.offset());
				KafkaMessage msg = new KafkaMessage(record.value());
				switch(KafkaTopics.of(record.topic())) {
				case DONE:
					done(msg);
				case ERROR:
					error(msg);
				case LOG:
					log(msg);
				}
			}
		}
	  
	  
	
	public static void log(KafkaMessage msg) {
		System.out.println(msg);
	}

	public static void error(KafkaMessage msg) {
		DataPreview.getBlocksPreview().get(msg.getBlock()).setStatus(Colors.ERROR);
	}

	public static void done(KafkaMessage msg) {
		DataPreview.getBlocksPreview().get(msg.getBlock()).setStatus(Colors.PROCESSED);
		IOFunctions.println("Block: "+msg.getBlock()+" finished!");

	}
}
