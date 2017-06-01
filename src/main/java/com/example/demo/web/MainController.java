package com.example.demo.web;

import com.example.demo.entity.Product;
import com.example.demo.entity.ResultMsg;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.catalina.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
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
    private ResultMsg msg;

    public MainController(ProductService ss) {
        this.ss = ss;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    String login(@RequestBody User user) {

        Key key = MacProvider.generateKey();

        String compactJws = Jwts.builder()
                .setSubject(user.getPassword())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        httpSession.setAttribute("key", key);
        httpSession.setMaxInactiveInterval(30);

        JSONObject jsb = new JSONObject();
        jsb.put("res_code", 200);
        jsb.put("msg", compactJws);
        jsb.put("jsessionid", httpSession.getId());

        return jsb.toString();
    }

    @RequestMapping(value = "/product/all", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findAll(HttpServletRequest request, HttpServletResponse res) {
        msg.setMsg(ss.queryProductAll());
        msg.setRes_code(200);
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
