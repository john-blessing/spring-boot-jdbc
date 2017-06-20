package com.example.demo.entity;

import org.springframework.stereotype.Component;

/**
 * Created by keifc on 2017/6/20.
 */
@Component
public class SimpleIoc {
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAccount(){

        return this.id + " : " + this.name;
    }
}
