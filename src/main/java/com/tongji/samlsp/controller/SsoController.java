package com.tongji.samlsp.controller;

import com.alibaba.fastjson.JSON;
import com.tongji.samlsp.model.PostDemo;
import com.tongji.samlsp.service.SAMLSPServlet;
import com.tongji.samlsp.service.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/4/28 18:17
 * @description：
 * @modified By：
 * @version:
 */
@RequestMapping("/v1/api")
// @RestController
@Controller
// @ResponseBody
@Slf4j
public class SsoController {
    @Autowired
    private SsoService ssoService;
    @Autowired
    private SAMLSPServlet samlspServlet;
    @Autowired
    private Environment env;


    @GetMapping("world")
    public String helloWorld(HttpServletRequest req, HttpServletResponse response) {
        // String samlPropertiesPath = env.getProperty("file.path");
        // log.info("samlPropertiesPath===>", samlPropertiesPath);
        //
        // log.info("------->hello world");
        // String uuid = ssoService.getUUID();
        // log.info("uuid = " + uuid);
        //
        // PostDemo p = new PostDemo();
        // p.setName("xm");
        // p.setAge(10);
        // String s = JSON.toJSONString(p);
        // log.info("jsonStr:[{}]", s);
        // // String res = OKHttpUtil.postJsonParams("http://127.0.0.1:9999/hello/post", s);
        // // log.info("res------------------>"+res);

        return "hello";
    }

    @PostMapping("post")
    public String helloPost(@RequestBody PostDemo postDemo) {
        log.info("------->hello post");
        System.out.println("postDemo = " + postDemo.getName() + "----" + postDemo.getAge());

        return JSON.toJSONString(postDemo);
    }

    @PostMapping("acs")
    public void acsPost(HttpServletRequest req, HttpServletResponse response) {
        log.info("------->hello acsPost");
        try {
            samlspServlet.doGet(req, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("acs")
    public void acsGet(HttpServletRequest req, HttpServletResponse response) {
        log.info("------->hello acsGet");
        try {
            samlspServlet.doGet(req, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
