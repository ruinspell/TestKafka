package com.snapdeal.request;

import java.util.List;

public class TopicConfigs {

    public List<SingleTopicConfig> getTopics() {
        return topics;
    }

    public void setTopics(List<SingleTopicConfig> topics) {
        this.topics = topics;
    }

    private List<SingleTopicConfig> topics;


    @Override
    public String toString() {
        return "TopicConfigs{" +
                "topics=" + topics +
                '}';
    }

}


