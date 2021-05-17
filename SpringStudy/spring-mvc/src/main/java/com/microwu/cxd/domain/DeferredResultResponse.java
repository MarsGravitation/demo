package com.microwu.cxd.domain;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/13  9:45
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Data
public class DeferredResultResponse {

    private Integer code;
    private String msg;

    public enum Msg {
        TIMEOUT("超时"),
        FAILED("失败"),
        SUCCESS("成功");

        @Getter
        private String desc;

        Msg(String desc) {
            this.desc = desc;
        }
    }

}
