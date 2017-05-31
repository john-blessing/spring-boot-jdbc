package com.example.demo.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by keifc on 2017/5/31.
 */
@Component
public class ResultMsg {
    private int res_code;
    private ArrayList msg;

    public int getRes_code() {
        return res_code;
    }

    public void setRes_code(int res_code) {
        this.res_code = res_code;
    }

    public ArrayList getMsg() {
        return msg;
    }

    public void setMsg(ArrayList msg) {
        this.msg = msg;
    }
}
