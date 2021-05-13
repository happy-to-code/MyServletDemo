package com.tongji.samlsp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    public static void main(String[] args) {
        IamgateWsResponse i = new IamgateWsResponse();
        i.setStatus("success");
        i.setCode(0);
        i.setMessage("mmm");
        System.out.println("i = " + i);
        System.out.println(i.getStatus() == "success");

    }
    private String status;
    private String message;
    private Integer code;

    public IamgateWsResponse() {
    }
}

