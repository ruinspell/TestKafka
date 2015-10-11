package com.snapdeal.request;

import java.util.List;

public class ConsumerModel {


    public List<SingleConsumerModel> getConsumerList() {
        return consumerList;
    }

    public void setConsumerList(List<SingleConsumerModel> consumerList) {
        this.consumerList = consumerList;
    }

    private List<SingleConsumerModel> consumerList;



}
