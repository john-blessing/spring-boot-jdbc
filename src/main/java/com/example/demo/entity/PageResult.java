package com.example.demo.entity;

public class PageResult<K, V> {
    private K list;
    private V total_count;

    public K getList() {
        return list;
    }

    public void setList(K list) {
        this.list = list;
    }

    public V getTotal_count() {
        return total_count;
    }

    public void setTotal_count(V total_count) {
        this.total_count = total_count;
    }
}
