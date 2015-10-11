package com.snapdeal.response;

/**
 * Created by root on 14/4/15.
 */
public class Response {

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private String responseMessage;

    public Response() {

    }

    @Override
    public String toString() {
        return "Response{" +
                "responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
