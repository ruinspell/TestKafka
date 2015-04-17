package com.snapdeal.request;


import java.util.List;

public class SimpleKafkaConfig {

    private String producerId;
    private String producerName;
    private String assignTopic;
    private String assignPort;
    private InputConfig inputConfig;
    private List<SimpleStringBasedPair> producerPropsConfig;

    public List<ConsumerConfig> getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(List<ConsumerConfig> consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    private List<ConsumerConfig> consumerConfig;

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getAssignTopic() {
        return assignTopic;
    }

    public void setAssignTopic(String assignTopic) {
        this.assignTopic = assignTopic;
    }

    public String getAssignPort() {
        return assignPort;
    }

    public void setAssignPort(String assignPort) {
        this.assignPort = assignPort;
    }

    public InputConfig getInputConfig() {
        return inputConfig;
    }

    public void setInputConfig(InputConfig inputConfig) {
        this.inputConfig = inputConfig;
    }

    public List<SimpleStringBasedPair> getProducerPropsConfig() {
        return producerPropsConfig;
    }

    public void setProducerPropsConfig(List<SimpleStringBasedPair> producerPropsConfig) {
        this.producerPropsConfig = producerPropsConfig;
    }


    @Override
    public String toString() {
        return "SimpleKafkaConfig{" +
                "producerId='" + producerId + '\'' +
                ", producerName='" + producerName + '\'' +
                ", assignTopic='" + assignTopic + '\'' +
                ", assignPort='" + assignPort + '\'' +
                ", inputConfig=" + inputConfig +
                ", producerPropsConfig=" + producerPropsConfig +
                ", consumerConfig=" + consumerConfig +
                '}';
    }

}
