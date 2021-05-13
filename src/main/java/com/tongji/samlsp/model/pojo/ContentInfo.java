package com.tongji.samlsp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/7 10:38
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
public class ContentInfo {
    private Employee employee;
    private User user;
    private List<Object> privilege;

    public ContentInfo() {
    }
}
