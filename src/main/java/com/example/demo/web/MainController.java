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
    public @ResponseBody String home(@PathVariable int id) {
        JSONObject jarr = new JSONObject(ss.queryProduct(id));
        return jarr.toString();
    }

    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody int home(@RequestBody Product product) {

        ss.saveProduct(product);

//        JSONObject jarr = new JSONObject(ss.queryProduct(product.getId()));
//
//        if(jarr != null){
//            return jarr.toString();
//        } else {
//            return "fail";
//        }
        return 200;
    }

}
