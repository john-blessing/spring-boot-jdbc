package com.example.demo.entity;

import org.springframework.stereotype.Component;

/**
 * Created by keifc on 2017/6/7.
 */

@Component
public class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
