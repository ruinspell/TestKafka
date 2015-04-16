package com.snapdeal.controller;

import com.snapdeal.consumer.Consumer;
import com.snapdeal.producer.Impl.ProducerImpl;
import com.snapdeal.request.PdpRequest;
import com.snapdeal.response.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String sayHello() {
        return "hello";
    }


    @RequestMapping(value = "/handleRequest",method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public Response handleRequest(@RequestBody PdpRequest request) {

        String event = request.getEvent();
        String value = request.getValue();
        Consumer firstConsumer = new Consumer("eventTopic");
        Consumer secondConsumer = new Consumer("valueTopic");

        try {
            firstConsumer.start();
            System.out.println("First Consumer have been started");
            secondConsumer.start();
            System.out.println("Second Consumer have been started");


            ProducerImpl producerFirst = new ProducerImpl("eventTopic", "9092", event);
            ProducerImpl producerSecond = new ProducerImpl("valueTopic", "9093", value);
            producerFirst.start();
            producerSecond.start();
        } catch (Exception e) {
            System.out.println("Some error have been occured");
            e.printStackTrace();
            Response response = new Response();
            response.setResponseMessage("Problem");
          //  return response;
        }
        Response response = new Response();
        response.setResponseMessage("Success");
        return response;
    }
}
