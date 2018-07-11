package com.example.demo.web;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.ResultMsg;
import com.example.demo.entity.User;
import com.example.demo.service.BaseServiceImp;
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
    private BaseServiceImp baseServiceImp;

    @Autowired
    private Base base;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg login(@RequestBody User user, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();

        Cookie cookie = new Cookie("dscj", base.createToken(user));
        cookie.setMaxAge(24 * 60 * 60 * 30);
        cookie.setDomain("");
        response.addCookie(cookie);

        resultMsg.setMsg("success");
        resultMsg.setRes_code(200);

        return resultMsg;
    }

    @RequestMapping(value = "/getClassRoom", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findClassRoom(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();

        if (request.getCookies() != null) {
            if (base.checkToken(request.getCookies())) {
                List<ClassRoom> list = baseServiceImp.findAllClassRoom();
                resultMsg.setMsg(list);
                resultMsg.setRes_code(200);
            } else {
                resultMsg.setMsg("token错误");
                resultMsg.setRes_code(-100);
            }
        } else {
            resultMsg.setMsg("token过期");
            resultMsg.setRes_code(-100);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public @ResponseBody
    User findUser(HttpServletRequest request) {
//            User user = baseServiceImp.findUser();

    }
}
