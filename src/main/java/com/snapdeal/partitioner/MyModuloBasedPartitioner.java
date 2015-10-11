package com.snapdeal.partitioner;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;


public class MyModuloBasedPartitioner implements Partitioner {

    public MyModuloBasedPartitioner (VerifiableProperties props) {

    }
    @Override
    public int partition(Object key, int partitionCount) {

        String keyAsString =    null;
        if(key instanceof String)
            keyAsString = (String)key;
        else
            throw new RuntimeException();
        return keyAsString.hashCode()%partitionCount;
    }
}
