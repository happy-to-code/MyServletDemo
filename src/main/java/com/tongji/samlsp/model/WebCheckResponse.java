package com.tongji.samlsp.model;

import lombok.*;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/7 14:02
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WebCheckResponse {
    private Integer code;
    private String message;
    private String data;

    public WebCheckResponse() {
    }
}
