package com.microwu.callback;

/**
 * Description: 主函数
 *  用第一个的思想来理解一下，主函数和回调函数是应用层，而控制器对象是系统函数
 *  控制器类引用了回调函数，相当于把回调函数传给了系统函数，系统函数进行某些判断后，会自动调用回调函数
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   10:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Program {

    public static void main(String[] args) {
        // 创建控制器对象，将提供给它的回调对象传入
        Controller controller = new Controller(new CallBackClass());
        // 启动控制器对象
        controller.begin();
    }

}