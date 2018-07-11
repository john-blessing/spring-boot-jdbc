package com.example.demo.web;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.ResultMsg;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVo;
import com.example.demo.service.UserServiceImp;
import com.example.demo.util.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value = "/api")
public class MainController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private Base base;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg login(@RequestBody UserVo userVo, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        User user1 = userServiceImp.findUser(userVo.getUsername(), base.encryptSHA(userVo.getPassword()));
        if (user1 != null) {
            Cookie cookie = new Cookie("dscj", base.createToken(user1));
            cookie.setMaxAge(24 * 60 * 60 * 30);
            response.addCookie(cookie);
            resultMsg.setContent(user1);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("没有相关用户");
            resultMsg.setRes_code(-1);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg register(@RequestBody UserVo userVo) {
        ResultMsg resultMsg = new ResultMsg();
        User user1 = userServiceImp.findUser(userVo.getUsername(), userVo.getPassword());
        if (user1 == null) {
            int rows = userServiceImp.register(userVo.getUsername(), base.encryptSHA(userVo.getPassword()));
            if (rows > 0) {
                resultMsg.setContent("注册成功!");
                resultMsg.setRes_code(200);
                base.sendEmail();
            } else {
                resultMsg.setContent("注册失败!");
                resultMsg.setRes_code(-100);
            }
        } else {
            resultMsg.setContent("已有相关用户是否查找密码");
            resultMsg.setRes_code(-1);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/getClassRoom", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findClassRoom(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        if (base.checkToken(request) > 0) {
            List<ClassRoom> list = userServiceImp.findAllClassRoom();
            resultMsg.setContent(list);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(-100);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg findUserById(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();
        if (base.checkToken(request) > 0) {
            User user = userServiceImp.findUserById(base.checkToken(request));
            resultMsg.setContent(user);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(-100);
        }
        return resultMsg;
    }

}
