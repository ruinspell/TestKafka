package com.snapdeal.producer;

import com.snapdeal.request.SingleProducerModel;
import org.apache.log4j.Logger;

import java.util.List;


public abstract class IProducer {

    private static final Logger SLOGGER = Logger.getLogger(IProducer.class);
    public void addProducers(List<SingleProducerModel> modelList) {

        SLOGGER.info("Going to add following producers :"+modelList);
        for(SingleProducerModel producerModel : modelList) {
            addProducer(producerModel);
        }
        SLOGGER.info("All producer have been done successfully");
    }

    public abstract void addProducer(SingleProducerModel singleProducerModel) ;

}
