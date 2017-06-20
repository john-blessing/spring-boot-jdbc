package com.example.demo.service;

//import com.example.demo.dao.ProductDao;

import com.example.demo.entity.Product;
import com.example.demo.entity.SimpleIoc;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by keifc on 2017/5/24.
 */

@Service
public class ProductService implements ProductServiceImpl {

    @Autowired
    public SimpleIoc simpleIoc;

    @Override
    @Transactional
    public int saveSecret(String user_id, String token) {
        // 判断时候数据库里存在
        int result = jdbcTemplate.update("INSERT IGNORE INTO user_secret VALUES(?,?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user_id);
                ps.setString(2, token);
            }
        });

        return result;
    }

    @Override
    public String getUserid(String token) {
        System.out.println(token);
        return jdbcTemplate.queryForObject("select user_id from user_secret where token = ?", new Object[]{token}, String.class);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Product queryProduct(String id) {
        RowMapper<Product> rowMapper = new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                Product p = new Product();
                p.setP_id(rs.getString("p_id"));
                p.setP_name(rs.getString("p_name"));
                p.setP_price(rs.getFloat("p_price"));
                p.setP_des(rs.getString("p_des"));
                return p;
            }
        };
        return jdbcTemplate.queryForObject("select * from female_style where p_id = ?", new Object[]{id}, rowMapper);
    }

    @Override
    public int queryProductCount(String id) {
        return jdbcTemplate.queryForObject("select COUNT(*) from female_style where p_id = ?", new Object[]{id}, int.class);
    }

    @Override
    @Transactional
    public int saveProduct(Product product) {
        // 判断时候数据库里存在
        int result = jdbcTemplate.update("INSERT INTO female_style VALUES(?,?,?,?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, product.getP_id());
                ps.setString(2, product.getP_name());
                ps.setFloat(3, product.getP_price());
                ps.setString(4, product.getP_des());
            }
        });

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

    @Override
    public void sendEmail() {
        Properties props = new Properties();
//        props.setProperty("mail.smtp.localhost", "mail.digu.com");
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        // qq邮箱需要通过ssl通道
        props.setProperty("mail.smtp.ssl.enable", "true");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        try {
            msg.setSubject("JavaMail测试");
            // 设置邮件内容
            msg.setText("hello world");

            // 设置发件人
            msg.setFrom(new InternetAddress("1585185302@qq.com"));

            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect("1585185302@qq.com", "olscvcxcecathagb");
            // 发送邮件
            transport.sendMessage(msg, new Address[] {new InternetAddress("jinjifu08@163.com")});
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> queryProductAll() {
        return (ArrayList) jdbcTemplate.queryForList("SELECT * FROM female_style");
    }
}

