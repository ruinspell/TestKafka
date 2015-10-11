package com.snapdeal.request;

public class SingleConsumerModel {

    private String topicConnect;
    private Integer kafkaStreamCount;



    @Override
    public String toString() {
        return "SingleConsumerModel{" +
                "topicConnect='" + topicConnect + '\'' +
                ", kafkaStreamCount=" + kafkaStreamCount +
                '}';
    }

    public String getTopicConnect() {
        return topicConnect;
    }

    public void setTopicConnect(String topicConnect) {
        this.topicConnect = topicConnect;
    }

    public Integer getKafkaStreamCount() {
        return kafkaStreamCount;
    }

    public void setKafkaStreamCount(Integer kafkaStreamCount) {
        this.kafkaStreamCount = kafkaStreamCount;
    }


}
