package com.snapdeal.request;


public class SingleProducerModel {


    private String topicConnect;
    private String input;
    private String brokerList;

    public String getTopicConnect() {
        return topicConnect;
    }

    public void setTopicConnect(String topicConnect) {
        this.topicConnect = topicConnect;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }


    @Override
    public String toString() {
        return "SingleProducerModel{" +
                "topicConnect='" + topicConnect + '\'' +
                ", input='" + input + '\'' +
                ", brokerList='" + brokerList + '\'' +
                '}';
    }
}
