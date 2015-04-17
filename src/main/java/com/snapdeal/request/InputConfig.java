package com.snapdeal.request;

/**
 * @sandeepandey
 */
public class InputConfig {

    @Override
    public String toString() {
        return "InputConfig{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
