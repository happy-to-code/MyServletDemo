package com.tongji.samlsp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/6 10:07
 * @description：
 * @modified By：
 * @version:
 */
@Setter
@Getter
@ToString
public class SSOContent {
    private String userIdentity;
    private String userAttributes;
    private String authnContext;
    private String content;

    public SSOContent() {
    }
}
