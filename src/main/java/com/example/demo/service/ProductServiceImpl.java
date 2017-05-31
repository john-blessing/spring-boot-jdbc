package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/28.
 */
public interface ProductServiceImpl {

     Product queryProduct(String id);

     int queryProductCount(String id);

     int saveProduct(Product product);

     int removeProduct(String id);

     int updateProduct(Product product);

     void sendEmail();

     ArrayList<Product> queryProductAll();
}
