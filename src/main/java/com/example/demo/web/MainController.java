package com.example.demo.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.entity.*;
import com.example.demo.service.BaseService;
import com.example.demo.service.BaseServiceImp;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;


import javax.servlet.http.*;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value = "/api")
public class MainController {

    private BaseServiceImp bs = new BaseServiceImp();

//    public Boolean validate(HttpServletResponse response) {
//
//        boolean flag = true;
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(ss.getUserid(token));
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .acceptLeeway(1)   //1 sec for nbf and iat
//                    .acceptExpiresAt(5)
//                    .withIssuer("auth0")
//                    .build(); //Reusable verifier instance
//            DecodedJWT jwt = verifier.verify(token);
//            flag = true;
//        } catch (UnsupportedEncodingException exception) {
//            //UTF-8 encoding not supported
//        } catch (JWTVerificationException exception) {
//            //Invalid signature/claims
//            flag = false;
//        }
//        return flag;
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    String login(@RequestBody User user, HttpServletResponse response) {
        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getUsername() + user.getPassword());
            token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }

        Cookie cookie = new Cookie("dscj", token);
        cookie.setDomain("");
        response.addCookie(cookie);

        System.out.println(cookie);
        JSONObject jsb = new JSONObject();
        jsb.put("res_code", 200);

        return jsb.toString();
    }
    @RequestMapping(value="/getClassRoom", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findClassRoom() {
        ResultMsg rm = new ResultMsg();
        rm.setRes_code(200);
        rm.setMsg(null);
        List<ClassRoom> list = bs.findClassRoom();
        System.out.println(list);
        return rm;
    }
}
