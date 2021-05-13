package com.tongji.samlsp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/7 10:31
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
public class Employee {
    private String organId;
    private String createTime;
    private String passwordType;
    private String status;
    private String updateTime;
    private String telphone;
    private String userType;
    private String id;
    private String certificatesType;
    private String email;
    private String certificatesNumber;
    private String address;
    private String name;
    private String uuid;
    private String organName;
    private String mobile;

    public Employee() {
    }
}
