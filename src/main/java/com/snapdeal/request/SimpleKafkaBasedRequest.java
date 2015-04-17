package com.snapdeal.request;

import java.util.List;

/**
 * Created by root on 16/4/15.
 */
public class SimpleKafkaBasedRequest {



    private List<SimpleKafkaConfig> kafkaConfig;


    public List<SimpleKafkaConfig> getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(List<SimpleKafkaConfig> kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }


    @Override
    public String toString() {
        return "SimpleKafkaBasedRequest{" +
                "kafkaConfig=" + kafkaConfig +
                '}';
    }


}


