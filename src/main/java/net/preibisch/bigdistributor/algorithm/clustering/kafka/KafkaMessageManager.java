package net.preibisch.bigdistributor.algorithm.clustering.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaMessageManager {
	//TO be adapted to communicate with GUI
	
	private static String jobId;
	private static int size;
	
	  public static void process(ConsumerRecord<String, String> record) {
		  System.out.println("Received message: (" + record.topic()+","+record.key() + ", " + record.value() + ") at offset " + record.offset());
			if(jobId.equals(record.key())) {
	        	
				KafkaMessage msg = new KafkaMessage(record.topic(),record.key(),record.value());
				switch(KafkaTopics.of(record.topic())) {
				case DONE:
					done(msg);
					break;
				case ERROR:
					error(msg);
					break;
				case LOG:
					log(msg);
					break;
		default:
					System.out.println("ERROR topic process: "+record.topic());
					break;
				}
			}else {
				System.out.println("Invalid id: "+jobId + " -"+ record.key()+" | (" + record.topic()+","+record.key() + ", " + record.value() + ")");
			}
		}
	  
	  
	
	public static void log(KafkaMessage msg) {
		System.out.println(msg);
	}

	public static int getSize() {
		return size;
	}
	
	public static void error(KafkaMessage msg) {
		System.out.println("ERROR ! "+msg);
//		DataPreview.getBlocksPreview().get(msg.getBlock()).setStatus(Colors.ERROR);
	}
	
	public static void done(KafkaMessage msg) {
		System.out.println("DONE ! "+msg);
//		DataPreview.getBlocksPreview().get(msg.getBlock()).setStatus(Colors.PROCESSED);
//		IOFunctions.println("Block: "+msg.getBlock()+" finished!");
	}
}
