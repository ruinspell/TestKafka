package com.snapdeal.topic.impl;


import com.snapdeal.properties.ZookeeperProps;
import com.snapdeal.request.SingleTopicConfig;
import com.snapdeal.topic.ITopicOperation;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import com.snapdeal.utils.thread.BoundryUtils;

import java.util.Properties;


public class TopicOperationImpl extends ITopicOperation {

    private static final Logger LOG = Logger.getLogger(TopicOperationImpl.class);

    //TODO: need to improve
    private static final ZkClient zkClient = new ZkClient(ZookeeperProps.zookeeperURL, ZookeeperProps.connectionTimeoutMs,
            ZookeeperProps.sessionTimeoutMs, ZKStringSerializer$.MODULE$);



    @Override
    protected void removeTopic(String topicName) {
        if(BoundryUtils.isNullOrEmpty(topicName)) {
            LOG.info("Topic Name is not valid :"+ topicName);
        }

        LOG.info("Going to delete topic :"+topicName);
        AdminUtils.deleteTopic(zkClient,topicName);
        LOG.info("Topic have been deleted successfully .");
    }

    @Override
    public void addTopic(SingleTopicConfig singleTopicConfig){
        LOG.info("Going to topic for (" + singleTopicConfig+")");
        AdminUtils.createTopic(zkClient, singleTopicConfig.getTopicName(), singleTopicConfig.getPartitionCount(),
                singleTopicConfig.getReplicationFactor(), new Properties());

        LOG.info("Topic have been created successfully for (" + singleTopicConfig+")");
    }
}
