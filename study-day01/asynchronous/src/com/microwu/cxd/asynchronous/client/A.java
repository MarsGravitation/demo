package com.microwu.cxd.asynchronous.client;

import com.microwu.cxd.asynchronous.callback.CallBack2;
import com.microwu.cxd.asynchronous.server.B;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   11:08
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class A implements CallBack2 {

    private B b;

    public A(B b) {
        this.b = b;
    }

    // A需要解决一个问题，所以他把问题交给B处理，给B单独创建一个线程，不影响A的运行
    public void ask(final String question) {
        System.out.println("A问了B一个问题");
        new Thread(() -> {
            // B想帮A处理东西，就必须知道谁让自己处理的，所以要传入A，也要知道A想处理什么，所以要传入question
            b.executeMessage(A.this, question);
        }).start();
        play();
    }

    public void play() {
        System.out.println("我要去玩游戏。。。");
    }

    // A拿到了B处理完成的结果，可以进行一些操作，比如把结果输出
    @Override
    public void solve(String result) {
        System.out.println("B告诉A的答案是：" + result);
    }
}