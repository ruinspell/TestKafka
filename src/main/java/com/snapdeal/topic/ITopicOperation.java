package com.snapdeal.topic;
import com.snapdeal.exception.SnapdealKafkaRunTimeException;
import com.snapdeal.request.SingleTopicConfig;
import org.apache.log4j.Logger;
import com.snapdeal.utils.thread.BoundryUtils;

import java.util.List;


public abstract class  ITopicOperation {

    private static final Logger LOG = Logger.getLogger(ITopicOperation.class);
    public void removeTopics(List<String> topicsToBeErase) {
        if(BoundryUtils.isNullOrEmpty(topicsToBeErase)) {
            throw new SnapdealKafkaRunTimeException();
        }

        LOG.info("Topics to be remove :"+topicsToBeErase);
        for(String topicName : topicsToBeErase)
            removeTopic(topicName);
    }

    protected abstract void removeTopic(String topicName);

    public abstract void addTopic(SingleTopicConfig topicConfigList) ;

    public void addTopic(List<SingleTopicConfig> topicConfigList) {

        if(BoundryUtils.isNullOrEmpty(topicConfigList)) {
            throw new SnapdealKafkaRunTimeException("Unable to create topic for config :"+topicConfigList);
        }
        for(SingleTopicConfig singleTopicConfig : topicConfigList) {
            addTopic(singleTopicConfig);
        }

    }
}
