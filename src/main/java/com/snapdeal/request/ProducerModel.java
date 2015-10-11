package com.snapdeal.request;

import java.util.List;


public class ProducerModel {

    private List<SingleProducerModel> producerList;

    public List<SingleProducerModel> getProducerList() {
        return producerList;
    }

    public void setProducerList(List<SingleProducerModel> producerList) {
        this.producerList = producerList;
    }

    @Override
    public String toString() {
        return "ProducerModel{" +
                "producerList=" + producerList +
                '}';
    }

}


