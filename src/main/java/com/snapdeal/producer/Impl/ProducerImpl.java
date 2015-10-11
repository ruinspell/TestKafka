package com.snapdeal.producer.Impl;


import com.snapdeal.producer.IProducer;
import com.snapdeal.request.SingleProducerModel;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;
import com.snapdeal.utils.thread.BoundryUtils;

import java.util.Properties;

public class ProducerImpl extends IProducer {

    private static final Logger SLOGGER = Logger.getLogger(ProducerImpl.class);
    private String topicConnect;
    private String input;
    private String brokerList;
    private String customMessage;
    private static int count    =   0;
    @Override
    public void addProducer(SingleProducerModel singleProducerModel) {

        this.topicConnect = singleProducerModel.getTopicConnect();
        this.brokerList = singleProducerModel.getBrokerList();
        this.input = singleProducerModel.getInput();

        if(BoundryUtils.isNullOrEmpty(topicConnect) ||
                BoundryUtils.isNullOrEmpty(brokerList) ||
                BoundryUtils.isNullOrEmpty(input)) {
            SLOGGER.info("Missing input for producer :"+"("+this.input+","+this.topicConnect+","+this.brokerList);
            throw new RuntimeException("Missing input for producer :"+"("+this.input+","+this.topicConnect+","+this.brokerList);
        }
        ProducerConfig producerConfig = buildProducerConfig();
        Producer<String,String> producer = new Producer<String,String>(producerConfig);

        customMessage = "Message send by producer ("+count+") is "+input;

        KeyedMessage<String,String> data = new KeyedMessage<String, String>(this.topicConnect,this.input,customMessage);
        SLOGGER.debug("<..........Going to send message from producer...........>");

        producer.send(data);

        SLOGGER.debug("Message have been send successfully by producer ("+count+")" + " to topic ("+this.topicConnect+")");
    }


    protected ProducerConfig buildProducerConfig() {
        Properties props = new Properties();
        props.put("metadata.broker.list", this.brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        props.put("partitioner.class","com.snapdeal.partitioner.MyModuloBasedPartitioner");
        return new ProducerConfig(props);
    }
}