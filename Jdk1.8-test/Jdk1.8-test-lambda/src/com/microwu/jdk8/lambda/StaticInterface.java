package com.microwu.jdk8.lambda;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/8/6   10:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface StaticInterface {

    /**
     * 实现接口的类或者子接口不会继承接口中的静态方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  16:13
     *
     * @param
     * @return  void
     */
    static void staticMethod() {
        System.out.println("static method");
    }

    /**
     * 是为了不影响实现类而产生的，如果一个接口增加了一个方法，
     * 所有的实现类都要实现这个方法，可能出现兼容性问题
     *
     * 如果默认方法不能满足某个需求，实现类可以覆盖默认方法，不用加default
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  16:14
     *
     * @param   	
     * @return  void
     */
    default void print() {
        System.out.println("hello default");
    }
}
