package com.example.demo.dao;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by keifc on 2017/5/27.
 */

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    // 按id查询
    Product findById(int id);
    // 删除
    void deleteById(int id);
}
