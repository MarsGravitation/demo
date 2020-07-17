package com.microwu.cxd.design.rookie.chain;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/20   16:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConcreteHandlerTwo extends Handler {
    @Override
    public void handleMessage(int type) {
        if(type == 2 || type == 5) {
            System.out.println("two deal with problem");
        } else {
            System.out.println("two can not deal");
            if(nextHandler != null) {
                nextHandler.handleMessage(type);
            } else {
                System.out.println("no people");
            }
        }
    }
}