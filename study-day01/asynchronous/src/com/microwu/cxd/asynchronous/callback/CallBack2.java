package com.microwu.cxd.asynchronous.callback;

/**
 * Description:     回调接口
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   11:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface CallBack2 {

    /**
     *  A处理结果的方法，为什么要写这个接口？
     *  因为可能不止A需要用到B的处理过程，如果很多地方需要使用到B
     *  那么传入B的方法就不可能只传A类，所以需要定义一个接口，
     *  传入B的处理方法的参数就是这个接口对象
     *
     * @author 成旭东               chengxudong@microwu.com
     * @date    2019/4/19  11:03
     *
     * @param   result
     * @return  void
     */
    public void solve(String result);
}
