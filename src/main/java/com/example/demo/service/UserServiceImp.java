package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClassRoom> findAllClassRoom() {
        String sql = "select * from class_room";

        List<ClassRoom> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClassRoom.class));

        return list;
    }

    @Override
    public User findUserById(int user_id) {
        String sql = "select * from user where user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{user_id}, new BeanPropertyRowMapper<>(User.class));
        return user;
    }

    @Override
    public User findUser(User user) {
        String sql = "select * from user where username = ? and password= ?";
        String username = user.getUsername();
        String password = user.getPassword();
        List<User> res = jdbcTemplate.query(sql, new Object[]{username, password}, new BeanPropertyRowMapper<>(User.class));
        return res.size() > 0 ? res.get(0) : null;
    }
}
