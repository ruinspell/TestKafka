package com.snapdeal;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class KafkaProducerPerformanceTest {


    public static void sendRequest(String str) {

        String urlStr = "http://localhost:8081/eventData";
        try {
            DefaultHttpClient httpClient = null;
            HttpPost postRequest = new HttpPost(urlStr);

            StringEntity input = new StringEntity(str);
            input.setContentType("application/json");
            postRequest.setEntity(input);

            for(int i = 0 ; i<10000 ; i++) {
                httpClient = new DefaultHttpClient();

                long startTime = System.currentTimeMillis();

                HttpResponse response = httpClient.execute(postRequest);

                long endTime = System.currentTimeMillis();
                System.out.println("Sending JSON Completed. Time take in MS " + (endTime-startTime));

                httpClient.getConnectionManager().shutdown();

            }

            //			BufferedReader br = new BufferedReader(
            //                    new InputStreamReader((response.getEntity().getContent())));
            //
            //			String output;
            //
            //			while ((output = br.readLine()) != null) {
            //				System.out.println(output);
            //			}



            httpClient.getConnectionManager().shutdown();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

