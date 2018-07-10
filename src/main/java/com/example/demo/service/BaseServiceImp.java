package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseServiceImp implements BaseService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Override
    public List<ClassRoom> findClassRoom() {
        System.out.println(jdbcTemplate);
        List<ClassRoom> list = jdbcTemplate.query("select * from class_room", new RowMapper<ClassRoom>() {
            @Override
            public ClassRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
                ClassRoom classRoom = new ClassRoom();
                classRoom.setC_id(rs.getInt("c_id"));
                classRoom.setC_count(rs.getInt("c_count"));
                classRoom.setC_name(rs.getString("c_name"));
                return classRoom;
            }
        });
        return list;
    }
}
