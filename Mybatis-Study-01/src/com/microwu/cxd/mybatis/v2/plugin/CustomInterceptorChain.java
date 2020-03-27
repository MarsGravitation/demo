package com.microwu.cxd.mybatis.v2.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 插件链存放类
 * Author:   Administration
 * Date:     2019/3/1 12:16
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomInterceptorChain {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }

    /**
     * @Descrip
     * @author 成旭东
     * @date 2019/3/1 12:22
     * @param  * @param target
     * @return java.lang.Object
     */
    public Object pluginAll(Object target){
        for(Interceptor interceptor : interceptors){
            target = interceptor.plugin(target);
        }
        return target;
    }

    public boolean hasPlugin(){
        return interceptors.size() != 0;
    }
}