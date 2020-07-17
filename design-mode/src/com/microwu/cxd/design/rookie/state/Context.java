package com.microwu.cxd.design.rookie.state;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/5   17:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Context {
    private State state;

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }
}