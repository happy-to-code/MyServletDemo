package com.tongji.samlsp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/7 16:57
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
public class IamgateWsResponse {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "jis后端运行";
        s = URLEncoder.encode(s, "utf-8");
        System.out.println("bytes = " + s.toString());
// %E5%90%8E%E7%AB%AF%E8%BF%90%E8%A1%8C
// %E5%90%8E%E7%AB%AF%E8%BF%90%E8%A1%8C
        String decode = URLDecoder.decode(s, "utf-8");
        System.out.println("decode = " + decode);
    }

    private String status;
    private String message;
    private Integer code;

    public IamgateWsResponse() {
    }
}

