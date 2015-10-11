package com.snapdeal.controller;

import com.snapdeal.consumer.ConsumerImpl;
import com.snapdeal.consumer.IConsumer;
import com.snapdeal.producer.IProducer;
import com.snapdeal.producer.Impl.ProducerImpl;
import com.snapdeal.properties.ZookeeperProps;
import com.snapdeal.request.*;
import com.snapdeal.response.Response;
import com.snapdeal.topic.ITopicOperation;
import com.snapdeal.topic.impl.TopicOperationImpl;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import scala.collection.Seq;
import com.snapdeal.utils.thread.BoundryUtils;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    private static final Logger SLOGGER = Logger.getLogger(WelcomeController.class);


    @RequestMapping(value = "/addProducer",method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public Response addProducers(@RequestBody ProducerModel producerModel) {
        IProducer iProducer = null;
        if(BoundryUtils.isNullOrEmpty(producerModel)) {
            SLOGGER.info("No consumer to be added.");
            throw new RuntimeException("No consumer to be added.");
        }
        iProducer = new ProducerImpl();

        List<SingleProducerModel> producerModels = producerModel.getProducerList();
        SLOGGER.info("Producer to be added are ("+producerModel+")");
        try {
            iProducer.addProducers(producerModels);
            SLOGGER.info("Producer have been added successfully.");
        } catch (Exception e) {
            SLOGGER.error("Problem while adding producer in the system.");
            throw new RuntimeException("Problem while adding producer in the system.");
        }

        Response response = new Response();
        response.setResponseMessage("Producer have been added successfully.");
        return response;
    }

    @RequestMapping(value = "/addConsumer",method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public Response addConsumers(@RequestBody ConsumerModel consumerModel) {
        IConsumer iConsumer = null;
        if(BoundryUtils.isNullOrEmpty(consumerModel)) {
            SLOGGER.info("There is no consumer to be add.");
            throw new RuntimeException("There is no consumer to be add.");
        }

        iConsumer = new ConsumerImpl();
        List<SingleConsumerModel> consumerModels = consumerModel.getConsumerList();
        SLOGGER.debug("Consumer to be added are ("+consumerModels+")");

        try {
            iConsumer.addConsumers(consumerModels);
            SLOGGER.info("Consumer have been added successfully.");
        } catch (Exception e) {
            SLOGGER.error("Problem while adding consumer in the system.");
            throw new RuntimeException("Problem while adding consumer in the system.");
        }

        Response response = new Response();
        response.setResponseMessage("Consumer have been added successfully.");
        return response;
    }


    @RequestMapping(value = "/removeTopics",method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public Response removeTopics(@RequestBody TopicDeleteConfig topicDeleteConfig)  {

        if(BoundryUtils.isNullOrEmpty(topicDeleteConfig)) {
            SLOGGER.info("Nothing to be delete :"+topicDeleteConfig);
            throw new RuntimeException();
        }

        SLOGGER.info("Starting Topics Deletion Operation." + topicDeleteConfig.getDeleteTopics());
        ITopicOperation topicOperation = new TopicOperationImpl();
        List<String> topics = topicDeleteConfig.getDeleteTopics();
        topicOperation.removeTopics(topics);
        SLOGGER.info("Removal of topics have been done successfully.");

        Response response  = new Response();
        response.setResponseMessage("Removal Topics is successful.");
        return response;
    }



    @RequestMapping(value = "/addTopics",method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public Response addTopics(@RequestBody TopicConfigs topicConfigs) {
        ITopicOperation topicOperation = null;
        List<SingleTopicConfig> topicsToBeAdd = null;

        if(BoundryUtils.isNullOrEmpty(topicConfigs)) {
            SLOGGER.info("Nothing to create topic :"+topicConfigs);
        }
        SLOGGER.info("Starting Topic Creation Operation for ("+topicConfigs+")");
        topicsToBeAdd = topicConfigs.getTopics();
        topicOperation = new TopicOperationImpl();
        topicOperation.addTopic(topicsToBeAdd);
        SLOGGER.info("Addition of topics have been done successfully.");


        Response response = new Response();
        response.setResponseMessage("Addition of topics have been performed successfully.");
        return response;
    }


    @RequestMapping(value = "/getBroker",method = RequestMethod.GET)
    @ResponseBody
    public Response getListOfBroker() throws IOException, KeeperException, InterruptedException {
        SLOGGER.debug("[Going to fetch broker list always.]");
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181",10000,null);

        String details="";
        List<String> brokerIds = zooKeeper.getChildren("/brokers/ids",false);
        for(String brokerId : brokerIds) {
            String brokerInfo = new String(zooKeeper.getData("/brokers/ids/" + brokerId, false, null));
            details+=brokerInfo+"\n";
            System.out.println("Broker Details Collected is :"+brokerInfo);
        }
        Response response = new Response();
        response.setResponseMessage("Broker Details have been collected: "+details);
        return response;
    }

    @RequestMapping(value = "/getTopics",method = RequestMethod.GET)
    @ResponseBody
    public Response getAllTopics() {
        ZkClient zkClient = new ZkClient(ZookeeperProps.zookeeperURL, ZookeeperProps.connectionTimeoutMs,
                ZookeeperProps.sessionTimeoutMs, ZKStringSerializer$.MODULE$);


        Seq<String> topics = ZkUtils.getAllTopics(zkClient);
        scala.collection.Iterator<String> topicIterator = topics.iterator();
        String allTopics = "";
        while(topicIterator.hasNext()) {
            allTopics+=topicIterator.next();
            allTopics+="\n";
        }

        Response response = new Response();
        response.setResponseMessage(allTopics);
        return response;

    }
}
