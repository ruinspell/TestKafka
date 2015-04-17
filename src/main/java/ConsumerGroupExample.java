import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsumerGroupExample {

    private final ConsumerConnector consumer;
    private final String topicName;
    private ExecutorService executor;
    private static String groupId;

    public ConsumerGroupExample(String topicName,String groupId) {

        this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(getConsumerConfig());
        this.topicName = topicName;
        this.groupId = groupId;
    }

    public static void main(String...s) {

        String topicName = "psan";
        String groupId = "groupp3";

        ConsumerGroupExample consumerGroupExample = new ConsumerGroupExample(topicName,groupId);
        consumerGroupExample.run(3);


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumerGroupExample.shutdown();


    }


    public static ConsumerConfig getConsumerConfig() {

        Properties properties = new Properties();
        properties.put("zk.connect","localhost:2181");
        properties.put("group.id",groupId);
        properties.put("zookeeper.session.timeout.ms", "400");
        properties.put("zookeeper.sync.time.ms", "200");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "smallest");
        return new ConsumerConfig(properties);
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
    }
    public void run(int threadCount)  {
        Map<String,Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(this.topicName,new Integer(threadCount));
        Map<String, List<KafkaStream<byte[], byte[]>>> kafkaStreams = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[],byte[]>> kafkaStreamList = kafkaStreams.get(this.topicName);

        // now launch all threads

        executor = Executors.newFixedThreadPool(threadCount);
        int threadNumber    =   0;
        for(KafkaStream kafkaStream : kafkaStreamList) {
            executor.submit(new ConsumerTest(kafkaStream,threadNumber));
            threadNumber++;
        }

    }



}


class ConsumerTest implements Runnable {

    private KafkaStream kafkaStream;
    private int threadNumber;

    public ConsumerTest(KafkaStream stream, int threadNumber) {
        this.kafkaStream = kafkaStream;
        this.threadNumber = threadNumber;
    }

    public void run() {

        ConsumerIterator<byte[],byte[]> consumerIterator = kafkaStream.iterator();
        while(consumerIterator.hasNext()) {
            System.out.println("Thread ("+this.threadNumber+" is reading ("+new String(consumerIterator.next().message()));
        }

        System.out.println("Lets Close Thread : "+this.threadNumber);
    }
}