package com.microwu.cxd.mybatis.v2.cache;

import java.lang.reflect.Array;

/**
 * Description: 缓存key值
 * Author:   Administration
 * Date:     2019/3/1 9:35
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CacheKey {
    // 沿用Mybatis设置的默认值
    private static final int DEFAULT_HASHCODE = 17;
    private static final int DEFAULT_MULTIPLYER = 37;

    private int hashcode;
    private int count;
    private int multiplyer;

    public CacheKey() {
        this.hashcode = DEFAULT_HASHCODE;
        this.count = 0;
        this.multiplyer = DEFAULT_MULTIPLYER;
    }

    /**
     * @Descrip 计算每一个传进来的object的code值
     * @author 成旭东
     * @date 2019/3/1 9:41
     * @param  * @param obejct
     * @return void
     */
    public void update(Object object){
        if(object != null && object.getClass().isArray()){
            int length = Array.getLength(object);
            for(int i = 0; i < length; i++){
                Object element = Array.get(object, i);
                doUpdate(element);
            }
        }else{
            doUpdate(object);
        }

    }

    public int getCode(){
        return this.hashcode;
    }

    public void doUpdate(Object o){
        int baseHashCode = o == null ? 1 : o.hashCode();
        count++;
        baseHashCode *= count;

        hashcode = multiplyer * hashcode + baseHashCode;
    }
}