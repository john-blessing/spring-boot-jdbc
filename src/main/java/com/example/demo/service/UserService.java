package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    // 查找所有的班级类型
    List<ClassRoom> findAllClassRoom();

    User findUserById(int user_id);

    User findUser(User user);
}
