package com.example.demo.service;

import com.example.demo.dao.ProductDao;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by keifc on 2017/5/24.
 */

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 查询
    public Product queryProduct(int id) {
        return productDao.findById(id);
    }

    // 插入
    @Transactional
    public void saveProduct(Product product) {
        jdbcTemplate.update("INSERT INTO PRODUCT VALUES(?,?,?,?,?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, product.getId());
                ps.setString(2, product.getName());
                ps.setFloat(3, product.getPrice());
                ps.setString(4, product.getDescription());
                ps.setInt(5, product.getOwn_id());
            }
        });
    }

    //删除
    @Transactional
    public String removeProduct(int id){
        productDao.deleteById(id);
        return "删除成功";
    }
}
