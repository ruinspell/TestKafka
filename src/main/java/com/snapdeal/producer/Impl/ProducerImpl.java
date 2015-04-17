package com.snapdeal.producer.Impl;

import com.snapdeal.producer.IProducer;
import com.snapdeal.request.InputConfig;
import com.snapdeal.request.SimpleKafkaConfig;
import com.snapdeal.request.SimpleStringBasedPair;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import utils.BoundryUtils;

import java.util.List;
import java.util.Properties;

/**
 * Created by root on 14/4/15.
 */
public class ProducerImpl extends Thread implements IProducer {

    private String producerId;
    private String producerName;
    private InputConfig inputConfig;
    private String assignedTopic;
    private String assignPort;
    private List<SimpleStringBasedPair> producerPropsConfig;
    private Producer<Integer, String> producer;
    private Properties props ;
    private ProducerConfig producerConfig;

    public ProducerImpl(SimpleKafkaConfig simpleKafkaConfig) {

        this.producerId = simpleKafkaConfig.getProducerId();
        this.producerName= simpleKafkaConfig.getProducerName();
        this.inputConfig = simpleKafkaConfig.getInputConfig();
        this.assignedTopic= simpleKafkaConfig.getAssignTopic();
        this.assignPort = simpleKafkaConfig.getAssignPort();
        this.producerPropsConfig = simpleKafkaConfig.getProducerPropsConfig();
        this.props = doInitProps();
        this.producerConfig = new ProducerConfig(this.props);
        this.producer = new Producer<Integer, String>(producerConfig);
    }

    private Properties doInitProps() {

        this.props = new Properties();
        if(BoundryUtils.isNullOrEmpty(producerPropsConfig)) {
            System.out.println("Going to init Default Config Values.");
            return goForDefaultConfig();
        }

        System.out.println("[Going to initiate custom Config for value]");
        for(SimpleStringBasedPair pair : producerPropsConfig) {
            props.put(pair.getParamKey(),pair.getParamValue());
        }
        return this.props;
    }

    private Properties goForDefaultConfig() {
        Properties l_properties = new Properties();
        l_properties.put("serializer.class","kafka.serializer.StringEncoder");
        l_properties.put("metadata.broker.list","localhost:8080");
        return l_properties;
    }

    public void run() {
        System.out.println("Producer ("+this.producerName+") is about to send message on topic ("+this.assignedTopic+")");
        KeyedMessage keyedMessage = new KeyedMessage(this.assignedTopic,this.inputConfig);
        producer.send(keyedMessage);
        System.out.println("[Data have been send successfully by producer ("+this.producerName+")");
    }
}
