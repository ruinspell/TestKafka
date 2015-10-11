package com.snapdeal.consumer;

import com.snapdeal.request.SingleConsumerModel;
import org.apache.log4j.Logger;
import com.snapdeal.utils.thread.BoundryUtils;

import java.util.List;


public abstract class IConsumer {

    private static final Logger SLOGGER = Logger.getLogger(IConsumer.class);

    public void addConsumers(List<SingleConsumerModel> consumers)  {

        if(BoundryUtils.isNullOrEmpty(consumers)) {
            SLOGGER.info("No Consumer is there to be add.");
        }

        for(SingleConsumerModel consumerModel : consumers) {
            SLOGGER.info("Going to add consumer :"+consumerModel);
            addConsumer(consumerModel);
            SLOGGER.info("Consumer have been added successfully.");
        }


    }

    public abstract void addConsumer(SingleConsumerModel consumerModel) ;
}
