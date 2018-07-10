package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class BaseServiceImp implements BaseService{

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClassRoom> findAllClassRoom() {
        String sql = "select * from class_room";

        List<ClassRoom> list = jdbcTemplate.query(sql, new RowMapper<ClassRoom>() {
            @Override
            public ClassRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
                ClassRoom classRoom = new ClassRoom();
                classRoom.setC_name(rs.getString("c_name"));
                classRoom.setC_id(rs.getInt("c_id"));
                classRoom.setC_count(rs.getInt("c_count"));
                return classRoom;
            }
        });

        return list;
    }
}
