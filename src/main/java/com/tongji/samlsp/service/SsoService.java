package com.tongji.samlsp.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/4/28 18:18
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class SsoService {
    public String getUUID() {
        String a = UUID.randomUUID().toString();
        return a;
    }


}
