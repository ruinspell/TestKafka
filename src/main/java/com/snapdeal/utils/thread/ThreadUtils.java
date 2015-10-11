package com.snapdeal.utils.thread;

import com.snapdeal.consumer.Consumer;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 20/4/15.
 */
public class ThreadUtils {

    private ExecutorService executorService;
    private int threadCount;

    public  ThreadUtils(){

    }

    public void  setUpThreadPool(int threadCount,List<KafkaStream<byte[],byte[]>> kafkaStreams,String groupName) {
        this.threadCount = threadCount;
        executorService = Executors.newFixedThreadPool(this.threadCount);
        int threadNumber    =   0;
        for(KafkaStream kafkaStream : kafkaStreams) {
            System.out.println("Going to launch thread ( Thread-"+threadNumber+")"+ "for group ("+groupName+")");
            Consumer consumer = new Consumer(threadNumber,kafkaStream,groupName);
            executorService.submit(consumer);
            System.out.println("Job have been submitted");
            threadNumber++;
        }
    }


    public void ensureSafeShutdown(ConsumerConnector consumerConnector) {

        if(consumerConnector != null)
            consumerConnector.shutdown();

        if(this.executorService != null) {
            this.executorService.shutdown();
        }

        try {
            if (!this.executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
    }


}
