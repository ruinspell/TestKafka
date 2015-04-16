package com.snapdeal.request;

/**
 * Created by root on 14/4/15.
 */


public class PdpRequest {

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public PdpRequest() {

    }


    @Override
    public String toString() {
        return "PdpRequest{" +
                "event='" + event + '\'' +
                ", value='" + value + '\'' +
                '}';
    }


}
