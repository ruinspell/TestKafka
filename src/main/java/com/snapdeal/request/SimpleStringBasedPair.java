package com.snapdeal.request;

/**
 * Created by root on 16/4/15.
 */
public class SimpleStringBasedPair {

    private String paramKey;
    private String paramValue;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String toString() {
        return "ProducerPropsConfig{" +
                "paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                '}';
    }
}
