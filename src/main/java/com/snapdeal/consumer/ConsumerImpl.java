package com.snapdeal.consumer;


import com.snapdeal.properties.KafkaProperties;
import com.snapdeal.request.SingleConsumerModel;
import com.snapdeal.utils.thread.ThreadUtils;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConsumerImpl extends IConsumer {

    private ConsumerConnector consumerConnector;
    private String topic;
    private SingleConsumerModel singleConsumerModel;


    public void addConsumer(SingleConsumerModel consumerModel) {
        initConnector();
        performAction();
    }

    private void performAction() {
        
        String topicName = singleConsumerModel.getTopicConnect();
        Integer kafkaStreamCount = singleConsumerModel.getKafkaStreamCount();
        Map<String,Integer> topicCountMap = new HashMap<>();

        topicCountMap.put(topicName,kafkaStreamCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams =  consumerMap.get(this.topic);

        ThreadUtils threadUtils = new ThreadUtils();
        threadUtils.setUpThreadPool(kafkaStreamCount,streams,null);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }

        threadUtils.ensureSafeShutdown(consumerConnector);

    }
    private void initConnector() {
        this.consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(
                buildConfig());
    }


    public ConsumerConfig buildConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
        props.put("group.id", KafkaProperties.groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }
}
