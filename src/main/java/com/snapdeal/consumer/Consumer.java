package com.snapdeal.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import java.util.concurrent.atomic.AtomicInteger;


public class Consumer implements Runnable {

    private static AtomicInteger count = new AtomicInteger(0);
    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    private int threadNumber;

    public KafkaStream getKafkaStream() {
        return kafkaStream;
    }

    public void setKafkaStream(KafkaStream kafkaStream) {
        this.kafkaStream = kafkaStream;
    }

    private KafkaStream kafkaStream;

    private String groupName;
    public Consumer(int threadNumber, KafkaStream kafkaStream,String groupName) {
        this.threadNumber = threadNumber;
        this.kafkaStream = kafkaStream;
        this.groupName = groupName;
    }

    @Override
    public void run() {

        ConsumerIterator<byte[],byte[]> consumerIterator = kafkaStream.iterator();
        while(consumerIterator.hasNext()) {
            try {
//                System.out.println("Consumer Thread from group ("+groupName+") "+ "("+threadNumber+")"+ "reading message ("+
//                        new String(consumerIterator.next().message(), "UTF-8") );
//
                count.incrementAndGet();
                System.out.println(count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
