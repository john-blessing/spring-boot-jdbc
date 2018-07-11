package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.User;

import java.util.List;

public interface BaseService{
    // 查找所有的班级类型
    List<ClassRoom> findAllClassRoom();

    User findUser(int user_id);
}
