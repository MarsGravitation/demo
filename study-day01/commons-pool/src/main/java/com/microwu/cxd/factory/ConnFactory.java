package com.microwu.cxd.factory;

import com.microwu.cxd.pojo.Conn;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Description: 为了使用 common-pool2 对象池管理，我们必须实现 PooledObjectFactory 或继承其子类，
 *      这是一个工厂模式，告诉对象池怎样去创建要管理的对象
 *
 * BasePooledObjectFactory 是对 PooledObjectFactory 的一个基本实现
 * 在实现 PooledObjectFactory 接口时，我们一定要实现的接口方法是 makeObject
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   15:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConnFactory extends BasePooledObjectFactory<Conn> {

    /**
     * 间接实现了 makeObject 方法，表明怎样创建需要管理的对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  15:29
     *
     * @param
     * @return  com.microwu.cxd.pojo.Conn
     */
    @Override
    public Conn create() throws Exception {
        return new Conn();
    }

    /**
     * 为了统计管理的对象的一些信息，比如调用次数，空闲时间，需要对管理的对象进行包赚，然后再放入对象池中
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  15:30
     *
     * @param   	conn
     * @return  org.apache.commons.pool2.PooledObject<com.microwu.cxd.pojo.Conn>
     */
    @Override
    public PooledObject<Conn> wrap(Conn conn) {
        return new DefaultPooledObject<>(conn);
    }
}