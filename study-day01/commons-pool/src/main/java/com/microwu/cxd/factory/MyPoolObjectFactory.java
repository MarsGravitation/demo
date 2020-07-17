package com.microwu.cxd.factory;

import com.microwu.cxd.pojo.Resource;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   14:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyPoolObjectFactory extends BasePooledObjectFactory<Resource> {
    /**
     * 创建一个对象实例
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  14:59
     *
     * @param
     * @return  com.microwu.cxd.pojo.Resource
     */
    @Override
    public Resource create() throws Exception {
        return new Resource();
    }

    /**
     * 包裹一个对象实例，范湖一个 pooled object
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  14:59
     *
     * @param   	resource
     * @return  org.apache.commons.pool2.PooledObject<com.microwu.cxd.pojo.Resource>
     */
    @Override
    public PooledObject<Resource> wrap(Resource resource) {
        return new DefaultPooledObject<>(resource);
    }
}