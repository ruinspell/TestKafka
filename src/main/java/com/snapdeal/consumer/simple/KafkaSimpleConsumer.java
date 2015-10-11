package com.snapdeal.consumer.simple;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.*;

public class KafkaSimpleConsumer {

    private List<String> m_replicaBrokers = new ArrayList<String>();

    public static void main(String args[]) {
        final KafkaSimpleConsumer example = new KafkaSimpleConsumer();

        final List<String> seeds = new ArrayList<String>();


        final String topic = "T";
        final long maxReads = 10;
        final int partition = 4;

        String broker1 = "127.0.0.1";
        seeds.add(broker1);


        final int port = 9092;

        try {

            for(int topicCount=  0;topicCount < 3 ; topicCount++) {
                String topicName = topic;
                topicName = topicName +  (topicCount + 1);
                final String finalTopic = topicName;
                System.out.println("Going to start Simple Consumer for topic ("+finalTopic+") for each partition.");
                int partitionCount = 3;
                for (int i = 0; i < partitionCount; i++) {
                    final int tmp = i;
                    System.out.println("Going for start simple consumer for partition (" + (i + 1) + ")");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                example.run(maxReads,finalTopic, tmp, seeds, port);
                            } catch (Exception e) {
                                System.out.println("Problem while starting simple consumer due to (" + e.getMessage() + ")");
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    System.out.println("\n\n");
                }

                System.out.println("Simple Consumers for topic ("+finalTopic+") have been started successfully.");
            }
        } catch (Exception e) {
            System.out.println("Oops:" + e);
            e.printStackTrace();
        }

    }

    public KafkaSimpleConsumer() {
        m_replicaBrokers = new ArrayList<String>();
    }

    public void run(long readMessageCount, String forTopic, int forPartition, List<String> brokers, int brokerAtPort) throws Exception {
        PartitionMetadata metadata = findLeader(brokers, brokerAtPort, forTopic, forPartition);
        if (metadata == null) {
            System.out.println("Can't find metadata for Topic and Partition. Exiting");
            return;
        }
        if (metadata.leader() == null) {
            System.out.println("Can't find Leader for Topic and Partition. Exiting");
            return;
        }
        String leadBroker = metadata.leader().host();
        String clientName = "Client_" + forTopic + "_" + forPartition;

        SimpleConsumer consumer = new SimpleConsumer(leadBroker, brokerAtPort, 100000, 64 * 1024, clientName);
        long readOffset = getLastOffset(consumer, forTopic, forPartition, kafka.api.OffsetRequest.LatestTime(), clientName);
        System.out.println("Offset now is "+readOffset);

        int numErrors = 0;
        while (true) {
            if (consumer == null) {
                consumer = new SimpleConsumer(leadBroker, brokerAtPort, 100000, 64 * 1024, clientName);
            }
            FetchRequest req = new FetchRequestBuilder().clientId(clientName)
                    .addFetch(forTopic, forPartition, readOffset, 100000) // Note: this fetchSize of 100000 might need to be increased if large batches are written to Kafka
                    .build();
            FetchResponse fetchResponse = consumer.fetch(req);

            if (fetchResponse.hasError()) {
                numErrors++;
                // Something went wrong!
                short code = fetchResponse.errorCode(forTopic, forPartition);
                System.out.println("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
                if (numErrors > 5) break;

                if (code == ErrorMapping.OffsetOutOfRangeCode()) {
                    System.out.println("Invalid Offset");
                    // We asked for an invalid offset. For simple case ask for the last element to reset
                    readOffset = getLastOffset(consumer, forTopic, forPartition, kafka.api.OffsetRequest.EarliestTime(), clientName);
                    continue;
                }
                consumer.close();
                consumer = null;
                leadBroker = findNewLeader(leadBroker, forTopic, forPartition, brokerAtPort);
                continue;
            }
            numErrors = 0;

            long numRead = 0;
            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(forTopic, forPartition)) {
                long currentOffset = messageAndOffset.offset();
                if (currentOffset < readOffset) {
                    System.out.println("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
                    continue;
                }
                readOffset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();

                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                System.out.println(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes, "UTF-8"));
                numRead++;
                readMessageCount--;
            }

            if (numRead == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        if (consumer != null) consumer.close();
    }

    public static long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
                partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out.println("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }

    private String findNewLeader(String a_oldLeader, String a_topic, int a_partition, int a_port) throws Exception {
        for (int i = 0; i < 3; i++) {
            boolean goToSleep = false;
            PartitionMetadata metadata = findLeader(m_replicaBrokers, a_port, a_topic, a_partition);
            if (metadata == null) {
                goToSleep = true;
            } else if (metadata.leader() == null) {
                goToSleep = true;
            } else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host())
                    && i == 0) {
                // first time through if the leader hasn't changed give
                // ZooKeeper a second to recover
                // second time, assume the broker did recover before failover,
                // or it was a non-Broker issue
                //
                goToSleep = true;
            } else {
                return metadata.leader().host();
            }
            if (goToSleep) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        System.out.println("Unable to find new leader after Broker failure. Exiting");
        throw new Exception(
                "Unable to find new leader after Broker failure. Exiting");
    }

    private PartitionMetadata findLeader(List<String> brokers, int port, String forTopic, int forPartition) {
        PartitionMetadata returnMetaData = null;
        loop: for (String seed : brokers) {
            SimpleConsumer consumer = null;
            try {
                consumer = new SimpleConsumer(seed, port, 100000, 64 * 1024,
                        "leaderLookup");
                List<String> topics = Collections.singletonList(forTopic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        if (part.partitionId() == forPartition) {
                            returnMetaData = part;
                            break loop;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error communicating with Broker [" + seed + "] to find Leader for [" + forTopic + ", " + forPartition + "] Reason: " + e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        if (returnMetaData != null) {
            m_replicaBrokers.clear();
            for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
                m_replicaBrokers.add(replica.host());
            }
        }
        return returnMetaData;
    }


    private static class PartitionThread implements Runnable {

        int forPartition;
        List<String> brokers;
        int maxReadCount;
        int port;


        public PartitionThread(int forPartition,List<String> brokers,int maxReadCount,int port) {
            this.forPartition = forPartition;
            this.brokers = brokers;
            this.maxReadCount = maxReadCount;
            this.port = port;
        }
        @Override
        public void run() {

        }


    }
}

