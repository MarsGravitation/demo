package com.microwu.cxd.asynchronous.server;

import com.microwu.cxd.asynchronous.callback.CallBack2;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   11:09
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class B {

    public void executeMessage(CallBack2 callBack2, String question) {
        System.out.println(callBack2.getClass() + "问了一个问题：" + question);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "答案是2";
        callBack2.solve(result);
    }
}