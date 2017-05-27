package com.example.demo.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by keifc on 2017/5/27.
 */

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private float price;
    private String name;
    private String description;
    private int own_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwn_id() {
        return own_id;
    }

    public void setOwn_id(int own_id) {
        this.own_id = own_id;
    }
}
