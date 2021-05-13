package com.tongji.samlsp.test;

import com.alibaba.fastjson.JSON;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/6 9:28
 * @description：
 * @modified By：
 * @version:
 */
public class Demo {
    public static void main(String[] args) {
        Boy b = new Boy();
        b.setAge(10);
        b.setName("xiaoMing");

        System.out.println("b = " + b);
        System.out.println("===============");
        String jsonString = JSON.toJSONString(b);
        System.out.println("jsonString = " + jsonString);
    }
}
