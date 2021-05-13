package com.tongji.samlsp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/7 10:35
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
public class User {
    private String ipType;
    private String organId;
    private String appId;
    private String createdTime;
    private String updateTime;
    private String sessionToken;
    private String name;
    private String userId;
    private String userStatus;
    private String subUserId;
    private String uuid;
    private String organName;

    public User() {
    }
}
