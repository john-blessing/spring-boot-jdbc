package com.example.demo.web;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

/**
 * Created by keifc on 2017/5/24.
 */
@RestController
@RequestMapping(value="/product")
public class MainController {

    private ProductService ss;

    public MainController(ProductService ss) {
        this.ss = ss;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public @ResponseBody String find(@PathVariable int id) {
        JSONObject jarr = new JSONObject(ss.queryProduct(id));
        return jarr.toString();
    }

    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String save(@RequestBody Product product) {
        ss.saveProduct(product);
        return "success";
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST, produces = "application/x-www-form-urlencoded;charset=UTF-8")
    public @ResponseBody String delete(@RequestParam int id){
        return ss.removeProduct(id);
    }

}
