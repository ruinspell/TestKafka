package com.snapdeal.properties;

/**
 * Created by root on 19/4/15.
 */
public interface ZookeeperProps {

    int sessionTimeoutMs = 10000;
    int connectionTimeoutMs = 10000;
    String zookeeperURL = "localhost:2181";


}
