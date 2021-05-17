package com.microwu.cxd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/14  9:44
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMsg<T> {

    private int code;

    private String msg;

    private T data;

}
