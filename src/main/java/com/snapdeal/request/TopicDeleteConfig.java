package com.snapdeal.request;


import java.util.List;

public class TopicDeleteConfig {

    private List<String> deleteTopics;

    @Override
    public String toString() {
        return "TopicDeleteConfig{" +
                "deleteTopics=" + deleteTopics +
                '}';
    }

    public List<String> getDeleteTopics() {
        return deleteTopics;
    }

    public void setDeleteTopics(List<String> deleteTopics) {
        this.deleteTopics = deleteTopics;
    }


}
