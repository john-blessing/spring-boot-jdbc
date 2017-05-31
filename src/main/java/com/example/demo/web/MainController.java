package com.example.demo.web;

import com.example.demo.entity.Product;
import com.example.demo.entity.ResultMsg;
import com.example.demo.service.ProductService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.ArrayList;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value="/api")
public class MainController {

    @Autowired
    private ProductService ss;

    @Autowired
    private ResultMsg msg;

    public MainController(ProductService ss) {
        this.ss = ss;
    }

    private Key key;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody String login(String username, String password, HttpServletResponse res) {

        key = MacProvider.generateKey();

        String compactJws = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        JSONObject jsb = new JSONObject();
        jsb.put("res_code", 200);
        jsb.put("msg", compactJws);

        return jsb.toString();
    }

    @RequestMapping(value="/product/all", method = RequestMethod.GET)
    public @ResponseBody ResultMsg findAll(HttpServletRequest req, HttpServletResponse res) {
        try {

            Jwts.parser().setSigningKey(this.key).parseClaimsJws(req.getHeader("Authorization"));

            //OK, we can trust this JWT
            msg.setMsg(ss.queryProductAll());
            msg.setRes_code(200);

        } catch (SignatureException e) {

            //don't trust the JWT!
            msg.setMsg(null);
            msg.setRes_code(100);
        }

        return msg;
    }

    @RequestMapping(value="/product/{p_id}", method = RequestMethod.GET)
    public @ResponseBody ResultMsg find(@PathVariable String p_id, HttpServletRequest req) {
        ArrayList list = new ArrayList();
        list.add(ss.queryProduct(p_id));
        msg.setMsg(list);
        msg.setRes_code(200);
        return msg;
    }

    @RequestMapping(value="/product/save", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResultMsg save(@RequestBody Product product) {
        if(ss.saveProduct(product) == 1){
            msg.setMsg(null);
            msg.setRes_code(200);
            return msg;
        } else {
            msg.setMsg(null);
            msg.setRes_code(100);
            return msg;
        }

    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public @ResponseBody ResultMsg delete(@RequestParam String p_id){
        if(ss.removeProduct(p_id) == 1){
            msg.setMsg(null);
            msg.setRes_code(200);
            return msg;
        } else {
            msg.setMsg(null);
            msg.setRes_code(100);
            return msg;
        }
    }

    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResultMsg update(@RequestBody Product product) {
        if(ss.updateProduct(product) == 1){
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
