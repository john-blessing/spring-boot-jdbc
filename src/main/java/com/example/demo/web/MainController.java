package com.example.demo.web;

import com.example.demo.entity.*;
import com.example.demo.service.UserServiceImp;
import com.example.demo.util.Base;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ResultMsg resultMsg;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg login(@RequestBody UserVo userVo, HttpServletResponse response) {
        User user1 = userServiceImp.findUser(userVo.getUsername(), base.encryptSHA(userVo.getPassword()));
        if (user1 != null) {
            Cookie cookie = new Cookie("dscj", base.createToken(user1));
            cookie.setMaxAge(24 * 60 * 60 * 30);
            response.addCookie(cookie);
            resultMsg.setContent(user1);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("没有相关用户");
            resultMsg.setRes_code(500);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ResultMsg register(@RequestBody UserVo userVo) {
        User user1 = userServiceImp.findUser(userVo.getUsername(), base.encryptSHA(userVo.getPassword()));
        if (user1 == null) {
            int rows = userServiceImp.register(userVo.getUsername(), base.encryptSHA(userVo.getPassword()));
            if (rows > 0) {
                resultMsg.setContent("注册成功!");
                resultMsg.setRes_code(200);
//                base.sendEmail();
            } else {
                resultMsg.setContent("注册失败!");
                resultMsg.setRes_code(500);
            }
        } else {
            resultMsg.setContent("已有相关用户是否查找密码");
            resultMsg.setRes_code(500);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/getClassRoom", method = RequestMethod.GET)
    public @ResponseBody
    ResultMsg findClassRoom(HttpServletRequest request) {
        if (base.checkToken(request) > 0) {
            List<ClassRoom> list = userServiceImp.findAllClassRoom();
            resultMsg.setContent(list);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(500);
        }

        return resultMsg;
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg findUserById(HttpServletRequest request) {
        if (base.checkToken(request) > 0) {
            User user = userServiceImp.findUserById(base.checkToken(request));
            resultMsg.setContent(user);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(500);
        }
        return resultMsg;
    }

    @RequestMapping(value = "/searchQuestions", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg searchQuestions(@RequestParam(name = "content") String content,
                              @RequestParam(name = "page_index") int page_index,
                              @RequestParam(name = "page_size") int page_size,
                              HttpServletRequest request) {
        if (base.checkToken(request) > 0) {
            List<Question> questions = userServiceImp.searchQuestions(content, page_index, page_size);
            long total_count = userServiceImp.findAllQuestionCount();
            PageResult result = new PageResult();
            result.setList(questions);
            result.setTotal_count(total_count);
            resultMsg.setContent(result);
            resultMsg.setRes_code(200);
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(500);
        }
        return resultMsg;
    }    
    
    @RequestMapping(value = "/createQuestion", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg createQuestion(@RequestBody Question question, HttpServletRequest request) {
        if (base.checkToken(request) > 0) {
            int rows = userServiceImp.createQuestion(question);
            if (rows > 0) {
                resultMsg.setContent("创建成功");
                resultMsg.setRes_code(200);
            } else {
                resultMsg.setContent("创建失败");
                resultMsg.setRes_code(200);
            }
        } else {
            resultMsg.setContent("token错误");
            resultMsg.setRes_code(500);
        }
        return resultMsg;
    }

    @RequestMapping(value = "/getQueryParamater", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg getQueryParamater(@RequestParam(name = "content") String content,  HttpServletRequest request) {
        resultMsg.setContent(content);
        resultMsg.setRes_code(200);
        return resultMsg;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg getQueryParamater(HttpServletResponse response) {
        Cookie cookie = new Cookie("dscj", "");
        response.addCookie(cookie);
        resultMsg.setContent(true);
        resultMsg.setRes_code(200);
        return resultMsg;
    }

    public void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (base.checkToken(request) > 0) {
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            String filePath = request.getSession().getServletContext().getRealPath("upload/imgs/");

            Thread thread = new Thread(() -> {
                try {
                    this.uploadFile(file.getBytes(), filePath, fileName);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            });
            thread.start();

            String avator = "http://192.168.1.242:8084/upload/imgs/" + fileName;

        } else {

        }
        return resultMsg;
    }

    @RequestMapping(value = "/getArticleById/{id}", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg getArticleById(@PathVariable("id") int id) {
        Map hashMap = userServiceImp.getArticleById(id);
        resultMsg.setContent(hashMap);
        resultMsg.setRes_code(200);
        return resultMsg;
    }
}
