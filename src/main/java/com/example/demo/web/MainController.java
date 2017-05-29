package com.example.demo.web;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value="/product")
public class MainController {

    @Autowired
    private ProductService ss;

    public MainController(ProductService ss) {
        this.ss = ss;
    }

    // 输入结果
    public String resultMsg(int res){
        JSONObject jsb = new JSONObject();
        if(res == 1){
            jsb.put("meg", "success");
            jsb.put("code", 200);
        } else {
            jsb.put("meg", "fail");
            jsb.put("code", 200);
        }

        return jsb.toString();
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public @ResponseBody List<Product> findAll() {
        return ss.queryProductAll();
    }

    @RequestMapping(value="/{p_id}", method = RequestMethod.GET)
    public @ResponseBody Product find(@PathVariable String p_id) {
        return ss.queryProduct(p_id);
    }

    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String save(@RequestBody Product product) {
        if(ss.saveProduct(product) == 1){
            return this.resultMsg(1);
        } else {
            return this.resultMsg(0);
        }

    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public @ResponseBody String delete(@RequestParam String p_id){
        if(ss.removeProduct(p_id) == 1){
            return this.resultMsg(1);
        } else {
            return this.resultMsg(0);
        }
    }

    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String update(@RequestBody Product product) {
        if(ss.updateProduct(product) == 1){
            return this.resultMsg(1);
        } else {
            return this.resultMsg(0);
        }
    }

}
