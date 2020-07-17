package com.microwu.jvm.classes;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/24   15:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Deprecated
public class ClassStruct implements Serializable {
    private int number = 3;

    public int getValue(){
        return number;
    }
}