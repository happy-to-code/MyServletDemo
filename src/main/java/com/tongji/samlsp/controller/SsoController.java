package com.tongji.samlsp.controller;

import com.alibaba.fastjson.JSON;
import com.tongji.samlsp.model.PostDemo;
import com.tongji.samlsp.service.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/4/28 18:17
 * @description：
 * @modified By：
 * @version:
 */
@RequestMapping("/acs/")
@RestController
@ResponseBody
@Slf4j
public class SsoController {
    @Autowired
    private SsoService ssoService;
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

        return "index";
    }

    @PostMapping("post")
    public String helloPost(@RequestBody PostDemo postDemo) {
        log.info("------->hello post");
        System.out.println("postDemo = " + postDemo.getName() + "----" + postDemo.getAge());

        return JSON.toJSONString(postDemo);
    }
}
