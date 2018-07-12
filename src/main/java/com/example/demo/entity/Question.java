package com.example.demo.entity;

import java.util.List;

public class Question {
    private int q_id;
    private String q_type;
    private String q_content;
    private List<Answer> q_answer;

    public int getQ_id() {
        return q_id;
    }

    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    public String getQ_type() {
        return q_type;
    }

    public void setQ_type(String q_type) {
        this.q_type = q_type;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }

    public List<Answer> getQ_answer() {
        return q_answer;
    }

    public void setQ_answer(List<Answer> q_answer) {
        this.q_answer = q_answer;
    }
}
