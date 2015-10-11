package com.snapdeal;

public class TestDataHandler {
  public static void main(String[] args) {
            ProducerTest producerTest = new ProducerTest();
            //String fileName = "/home/sandeep/datafiles/clickStreamData.txt";
            //producerTest.sendToProducer(fileName);
            for(int i   =   0;i < 1 ; i++) {
                String fileName = "/home/sandeep/datafiles/Notes/delivery_test_prod.txt";
                producerTest.sendToProducer(fileName);
            }

  }

        public void decode() throws Exception{
            byte[] bytes = "hello".getBytes();
            String decoded = new String(bytes,"UTF-8");
        }
}
