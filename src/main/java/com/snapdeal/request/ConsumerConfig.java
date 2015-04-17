package com.snapdeal.request;

import java.util.List;

public class ConsumerConfig {
    private String consumerId;
    private String consumerName;
    private String assignTopic;
    private List<SimpleStringBasedPair> props;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getAssignTopic() {
        return assignTopic;
    }

    public void setAssignTopic(String assignTopic) {
        this.assignTopic = assignTopic;
    }

    public List<SimpleStringBasedPair> getProps() {
        return props;
    }

    public void setProps(List<SimpleStringBasedPair> props) {
        this.props = props;
    }



    @Override
    public String toString() {
        return "ConsumerConfig{" +
                "consumerId='" + consumerId + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", assignTopic='" + assignTopic + '\'' +
                ", props=" + props +
                '}';
    }

}
