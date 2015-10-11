package com.snapdeal.topic;

import com.snapdeal.properties.ZookeeperProps;
import com.snapdeal.request.SingleTopicConfig;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Properties;

public class DynamicTopicCreator {

    private ZkClient zkClient;
    public void createTopics() {


        List<SingleTopicConfig> topicConfigList = null;
        //simpleKafkaBasedRequest.getTopics();;
        createTopics(topicConfigList);
    }

    private void createTopics(List<SingleTopicConfig> topicConfigs) {
        System.out.println("[Going to create topics ] ("+topicConfigs+")");
        for(SingleTopicConfig singleTopicConfig : topicConfigs) {
            System.out.println("Going to create topic :"+singleTopicConfig);
            createTopic(singleTopicConfig);
            System.out.println("Topic creation have been created "+singleTopicConfig+" successfully");
        }
    }

    private void createTopic(SingleTopicConfig singleTopicConfig) {
        ZkClient zkClient = new ZkClient(ZookeeperProps.zookeeperURL, ZookeeperProps.connectionTimeoutMs,
                ZookeeperProps.sessionTimeoutMs, ZKStringSerializer$.MODULE$);

        AdminUtils.createTopic(zkClient, singleTopicConfig.getTopicName(), singleTopicConfig.getPartitionCount(),
                singleTopicConfig.getReplicationFactor(), new Properties());


    }

}
