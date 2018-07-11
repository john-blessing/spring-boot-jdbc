package com.example.demo.entity;

import org.springframework.stereotype.Component;

/**
 * Created by keifc on 2017/5/31.
 */
@Component
public class ResultMsg<K, V> {
    private K res_code;
    private V content;

    public K getRes_code() {
        return res_code;
    }

    public void setRes_code(K res_code) {
        this.res_code = res_code;
    }

    public V getContent() {
        return content;
    }

    public void setContent(V content) {
        this.content = content;
    }
}
