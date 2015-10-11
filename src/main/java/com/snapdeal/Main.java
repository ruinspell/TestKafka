package com.snapdeal;

import com.snapdeal.properties.KafkaProperties;
import com.snapdeal.properties.ZookeeperProps;
import com.snapdeal.utils.thread.ThreadUtils;
import kafka.admin.AdminUtils;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    private String firstTopicName = "first_test_topic_1";
    private String secondTopicName = "second_test_topic_1";
    private int defaultMessageSendByProducer = 100;
    private String firstConsumerGroupName = "first_group";
    private String secondConsumerGroupName = "second_group";
    private Integer defaultPartitionCount = 3;
    private Integer defaultReplicationFactor = 1;
    private static ZkClient zkClient = null;
    private String brokerList = "localhost:9092";
    private Producer<String,String> firstProducer;
    private Producer<String,String> secondProducer;

    static {

        zkClient = new ZkClient(ZookeeperProps.zookeeperURL, ZookeeperProps.connectionTimeoutMs,
                ZookeeperProps.sessionTimeoutMs, ZKStringSerializer$.MODULE$);
    }


    public static void main(String...s) {
        System.out.println("Hello");
        Main m = new Main();
        m.startKafkaScheduler();
    }

    private void startKafkaScheduler() {
//        try {
//            showTopics();
//        } catch (Exception e) {
//
//        }
//        createTopic(firstTopicName);
//        createTopic(secondTopicName);
        startConsumerGroup("offsetest","offsetest");
        //startConsumerGroup(secondConsumerGroupName, secondTopicName);
//        createProducers();
//        runProducerRandomly();

    }

    private void showTopics() throws IOException, KeeperException, InterruptedException {
       ZooKeeper zk = new ZooKeeper("localhost:2181", 10000, null);
        List<String> topics = zk.getChildren("/brokers/topics",null);
        for (String topic : topics) {
            System.out.println(topic);
        }
    }

    private void createProducers() {
        addProducer();
    }

    public void createTopic(String topicName) {
            AdminUtils.createTopic(zkClient,topicName,defaultPartitionCount,defaultReplicationFactor,new Properties());
        System.out.println("[Topic have been created successfully.]");
    }

    public void addProducer() {
        ProducerConfig producerConfig = buildProducerConfig();
        Producer<String,String> producer = new Producer<String,String>(producerConfig);
        if(firstProducer == null) {
            this.firstProducer = producer;
            return;
        } if(secondProducer == null) {
            this.secondProducer = producer;
        }
    }

    public void runProducerRandomly() {

        for(int i=  0;i < 25 ;i++) {
            String randomTopic;

            int randomValue = (int) (Math.random() * 2 + 1);

            if (randomValue == 1) {
                randomTopic = firstTopicName;
            } else {
                randomTopic = secondTopicName;
            }
            System.out.println("Random Topic is :"+randomTopic);
            String messageToBeSend = "This message is for topic ("+randomTopic+")";

            KeyedMessage<String,String> data = new KeyedMessage<String, String>(randomTopic,null,messageToBeSend);
            firstProducer.send(data);
        }
    }
    public void startConsumerGroup(String groupName,String topicName) {

        performAction(groupName,topicName);
    }


    protected ProducerConfig buildProducerConfig() {
        Properties props = new Properties();
        props.put("metadata.broker.list", this.brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        return new ProducerConfig(props);
    }


    private void performAction(String groupName,String topicName) {

        Integer kafkaStreamCount = defaultPartitionCount;
        Map<String,Integer> topicCountMap = new HashMap<>();
        ConsumerConnector consumerConnector = initConnector(groupName);

        topicCountMap.put(topicName,kafkaStreamCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams =  consumerMap.get(topicName);

        ThreadUtils threadUtils = new ThreadUtils();
        threadUtils.setUpThreadPool(kafkaStreamCount,streams,groupName);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
    }


    private ConsumerConnector initConnector(String groupName) {
        kafka.javaapi.consumer.ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(
                buildConfig(groupName));
        return consumerConnector;
    }


    public ConsumerConfig buildConfig(String groupName) {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
            props.put("group.id", groupName);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }
}



