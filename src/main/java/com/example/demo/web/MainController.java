package com.example.demo.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.*;
import com.example.demo.service.ProductService;
import org.apache.catalina.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value = "/api")
public class MainController {

    @Autowired
    private ProductService ss;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private HelloMessage hm;

    @Autowired
    private ResultMsg msg;

    @Autowired
    private SimpMessagingTemplate template;

    public MainController(ProductService ss) {
        this.ss = ss;
    }

    public Boolean validate(String token) {
        boolean flag = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256(ss.getUserid(token));
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            flag = true;
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            flag = false;
        }
        return flag;
    }

    @RequestMapping("/home")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

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

        ss.saveSecret(user.getUsername() + user.getPassword(), token);
        JSONObject jsb = new JSONObject();
        jsb.put("res_code", 200);
        jsb.put("msg", token);

//        hm.setName("hello");
//
//        try {
//            this.greeting(hm);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return jsb.toString();
    }

    @RequestMapping(value = "/product/all", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findAll(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            if (this.validate(request.getHeader("Authorization"))) {
                msg.setMsg(ss.queryProductAll());
                msg.setRes_code(200);
            } else {
                msg.setMsg(null);
                msg.setRes_code(-999);
            }
        } else {
            Cookie[] cookie = request.getCookies();

            msg.setMsg(null);
            msg.setRes_code(-999);
        }

        return msg;
    }

    @RequestMapping(value = "/product/{p_id}", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg find(@PathVariable String p_id, HttpServletRequest req) {
        ArrayList list = new ArrayList();
        list.add(ss.queryProduct(p_id));
        msg.setMsg(list);
        msg.setRes_code(200);
        return msg;
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg save(@RequestBody Product product) {
        if (ss.saveProduct(product) == 1) {
            msg.setMsg(null);
            msg.setRes_code(200);
            return msg;
        } else {
            msg.setMsg(null);
            msg.setRes_code(100);
            return msg;
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg delete(@RequestParam String p_id) {
        if (ss.removeProduct(p_id) == 1) {
            msg.setMsg(null);
            msg.setRes_code(200);
            return msg;
        } else {
            msg.setMsg(null);
            msg.setRes_code(100);
            return msg;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg update(@RequestBody Product product) {
        if (ss.updateProduct(product) == 1) {
            msg.setMsg(null);
            msg.setRes_code(200);
            return msg;
        } else {
            msg.setMsg(null);
            msg.setRes_code(100);
            return msg;
        }
    }

}
