package com.microwu.cxd.design.rookie.chain;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/20   16:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public abstract class Handler {
    /**
     * 下一个责任链成员
     */
    protected Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 处理传递过来的信息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/20  16:09
     *
     * @param   	type
     * @return  void
     */
    public abstract  void handleMessage(int type);
}