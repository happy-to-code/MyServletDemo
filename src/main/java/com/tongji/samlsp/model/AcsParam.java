package com.tongji.samlsp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：zhangyifei
 * @date ：Created in 2021/5/6 13:32
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
@ToString
public class AcsParam {
    private String samlResponseParam;

    public AcsParam() {
    }
}
