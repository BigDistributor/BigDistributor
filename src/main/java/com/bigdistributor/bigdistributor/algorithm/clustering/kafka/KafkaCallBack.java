package net.preibisch.bigdistributor.algorithm.clustering.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaCallBack implements Callback {

    private final long startTime;
    private final String key;
    private final String message;

    public KafkaCallBack(long startTime, String key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    public KafkaCallBack(KafkaMessage message) {
    	this.startTime = message.getTimestamp();
        this.key = message.getJobid();
        this.message = message.toString();
	}

	/**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    @Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                    "), " +
                    "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}