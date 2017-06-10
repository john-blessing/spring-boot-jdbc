package com.example.demo.web;

import com.example.demo.entity.Greeting;
import com.example.demo.entity.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by keifc on 2017/6/7.
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/toppic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
