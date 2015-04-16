package com.snapdeal.producer.Impl;

import com.snapdeal.producer.IProducer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


import java.util.Properties;

/**
 * Created by root on 14/4/15.
 */
public class ProducerImpl extends Thread implements IProducer {
    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final Properties props = new Properties();
    private String requestData;
    public ProducerImpl(String topic,String assignedPort,String requestData)
    {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "localhost:"+assignedPort);
        // Use random partitioner. Don't need the key type. Just set it to Integer.
        // The message is of type String.
        producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
        this.requestData = requestData;
    }

    public void run() {

       while(true) {
            String messageStr = new String(this.topic);
            System.out.println("Producer is bind with topic."+messageStr);
            producer.send(new KeyedMessage<Integer, String>(this.topic, messageStr+" : and sending data :"+this.requestData));

        }
    }


}
