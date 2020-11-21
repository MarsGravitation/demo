package com.microwu.callback;

import java.util.Scanner;

/**
 * Description: 控制器类
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   10:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Controller {

    /**
     * 回调对象
     */
    private ICallBack callBack = null;
    /**
     * 读取命令行输入
     */
    Scanner input = new Scanner(System.in);

    public Controller(ICallBack callBack) {
        this.callBack = callBack;
    }

    public void begin() {
        while(input.next() != null) {
            callBack.run();
            break;
        }
    }

}