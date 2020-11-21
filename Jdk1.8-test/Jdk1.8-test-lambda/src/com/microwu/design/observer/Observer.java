package com.microwu.design.observer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   14:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface Observer {

    /**
     * 更新观察者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  15:01
     *
     * @param
     * @return  void
     */
    void update();

    /**
     * 附着主题到观察者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/28  15:01
     *
     * @param   	subject
     * @return  void
     */
    void setSubject(Subject subject);

}