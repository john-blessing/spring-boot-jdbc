package com.example.demo.service;

//import com.example.demo.dao.ProductDao;

import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keifc on 2017/5/24.
 */

@Service
public class ProductService implements ProductServiceImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> queryProductAll() {
        List<Product> list = new ArrayList<Product>();

        jdbcTemplate.query("select * from female_style", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    Product p = new Product();
                    p.setP_id(rs.getString("p_id"));
                    p.setP_name(rs.getString("p_name"));
                    p.setP_price(rs.getFloat("p_price"));
                    p.setP_des(rs.getString("p_des"));
                    list.add(p);
                }
            }
        });

        return list;
    }

    @Override
    public Product queryProduct(String id) {
        Product p = new Product();
        jdbcTemplate.query("select * from female_style where p_id = ?", new Object[]{id}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println(rs.next());
                while (rs.next()){
                    p.setP_id(rs.getString("p_id"));
                    p.setP_name(rs.getString("p_name"));
                    p.setP_price(rs.getFloat("p_price"));
                    p.setP_des(rs.getString("p_des"));
                }
            }
        });
        return p.getP_id() == null ? null : p;
    }

    @Override
    public int queryProductCount(String id) {
        return jdbcTemplate.queryForObject("select COUNT(*) from female_style where p_id = ?", new Object[]{id}, int.class);
    }

    @Override
    @Transactional
    public int saveProduct(Product product) {
        int count = this.queryProductCount(product.getP_id());
        int result = -1;

        // 判断时候数据库里存在
        if (count == 0) {
            result = jdbcTemplate.update("INSERT INTO female_style VALUES(?,?,?,?)", new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, product.getP_id());
                    ps.setString(2, product.getP_name());
                    ps.setFloat(3, product.getP_price());
                    ps.setString(4, product.getP_des());
                }
            });
        } else if (count > 1) {
            this.removeProduct(product.getP_id());
        }

        return result;
    }

    @Override
    @Transactional
    public int removeProduct(String id) {
        return jdbcTemplate.update("DELETE FROM female_style WHERE p_id = ?", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, id);
            }
        });
    }

    @Override
    @Transactional
    public int updateProduct(Product product) {
        return jdbcTemplate.update("UPDATE female_style SET p_id=?, p_name=?, p_price=?, p_des=? WHERE p_id = ?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, product.getP_id());
                        ps.setString(2, product.getP_name());
                        ps.setFloat(3, product.getP_price());
                        ps.setString(4, product.getP_des());
                        ps.setString(5, product.getP_id());
                    }
                });
    }
}
