package com.example.demo.web;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.ResultMsg;
import com.example.demo.entity.User;
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
    ResultMsg login(@RequestBody User user, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        User user1 = userServiceImp.findUser(user);
        if (user1 != null) {
            Cookie cookie = new Cookie("dscj", base.createToken(user1));
            cookie.setMaxAge(60);
            cookie.setDomain("");
            response.addCookie(cookie);
            resultMsg.setContent(user1);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("没有相关用户");
            resultMsg.setRes_code(-1);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/getClassRoom", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findClassRoom(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        if (base.checkToken(request)) {
            List<ClassRoom> list = userServiceImp.findAllClassRoom();
            resultMsg.setContent(list);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(-100);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/current/{user_id}", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg findUserById(@PathVariable String user_id, HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();
        if (base.checkToken(request)) {
            User user = userServiceImp.findUserById(Integer.parseInt(user_id));
            resultMsg.setContent(user);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(-100);
        }
        return resultMsg;
    }
}
