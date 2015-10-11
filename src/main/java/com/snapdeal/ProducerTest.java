package com.snapdeal;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class ProducerTest {


    public void sendToProducerByPost(String dataToSend) throws IOException {
        try {
            String type = "application/json";
            String encodedData = URLEncoder.encode(dataToSend);
            URL u = new URL("http://localhost:8081/eventData");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
            OutputStream os = conn.getOutputStream();
            os.write(encodedData.getBytes());
        } catch (Exception ex) {
            System.out.println("Problem while sending request "+ ex.getLocalizedMessage());
        }
    }

    public void sendToProducer(String fileName) {

        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("topic.metadata.refresh.interval.ms","500");

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                String str = refineDataLine(sCurrentLine);
                try {
                    KeyedMessage<String,String> data = new KeyedMessage<String, String>("delivery_test",str);
                    producer.send(data);
                    
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR IN FILE HANDELING ------------------------------->");
        }
    }

    private static String refineDataLine(String input) {
        return input;
    }
}
