package com.example.demo.entity;

import org.springframework.stereotype.Component;

/**
 * Created by keifc on 2017/6/7.
 */

@Component
public class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
