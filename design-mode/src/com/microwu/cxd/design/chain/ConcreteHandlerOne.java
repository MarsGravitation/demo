package com.microwu.cxd.design.chain;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/20   16:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConcreteHandlerOne extends Handler {
    @Override
    public void handleMessage(int type) {
        if(type == 1 || type == 3) {
            System.out.println("one deal with problem");
        } else {
            System.out.println("one can not deal");
            if(nextHandler != null) {
                nextHandler.handleMessage(type);
            } else {
                System.out.println("no people");
            }
        }
    }
}