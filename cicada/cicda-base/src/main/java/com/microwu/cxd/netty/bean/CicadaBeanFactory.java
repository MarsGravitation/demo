package com.microwu.cxd.netty.bean;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   11:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface CicadaBeanFactory {

    /**
     * Register into bean Factory
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/19  11:20
     *
     * @param   	object
     * @return  void
     */
    void register(Object object);

    /**
     * Get bean from bean Factory
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/19  11:21
     *
     * @param   	name
     * @return  java.lang.Object
     */
    Object getBean(String name) throws Exception;

    /**
     * get bean by class type
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/19  11:22
     *
     * @param   	clazz
     * @return  T
     */
    <T> T getBean(Class<T> clazz) throws Exception;

    /**
     * release all beans
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/19  11:22
     *
     * @param
     * @return  void
     */
    void releaseBean();

}