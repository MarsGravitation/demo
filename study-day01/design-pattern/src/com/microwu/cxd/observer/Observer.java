package com.microwu.cxd.observer;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   13:46
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface Observer {

    // 当状态改变时，主题会把这些状态值当作方法的参数，传递给观察者
    public void update(float temp, float humidity, float pressure);

}
