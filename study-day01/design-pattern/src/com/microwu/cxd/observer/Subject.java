package com.microwu.cxd.observer;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   13:43
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface Subject {

    // 这两个方法都需要一个观察者作为变量，该观察者是用来注册或者删除
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);

    // 当主题状态改变时，这个方法会被调用，已通知所有的观察者
    public void notifyObservers();

}
