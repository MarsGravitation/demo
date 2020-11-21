package com.microwu.design.observer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   14:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface Subject {

    /**
     * 注册观察者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  14:59
     *
     * @param   	observer
     * @return  void
     */
    void register(Observer observer);

    /**
     * 注销观察者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  14:59
     *
     * @param   	observer
     * @return  void
     */
    void unregister(Observer observer);

    /**
     * 通知所有观察者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  15:00
     *
     * @param
     * @return  void
     */
    void notifyObservers();

    /**
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  15:00
     *
     * @param   	observer
     * @return  java.lang.Object
     */
    Object getUpdate(Observer observer);

}