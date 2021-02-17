package com.microwu.cxd.common.utils;

import com.microwu.cxd.common.exception.ServiceException;

import java.util.Collection;

import static com.microwu.cxd.common.enums.StatusEnum.SERVER_NOT_AVAILABLE;

/**
 * Description:
 *  1. 泛型类 > class 类名称 <泛型标识>
 *  2. 泛型方法 > 其他修饰符 <泛型标识> 返回值，经测试，必须放在返回值前面，之间不能有其他关键字
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CollectionUtil {

    public static <T> void isEmpty(Collection<T> collection) {
        if (collection == null || collection.size() == 0) {
            throw new ServiceException(SERVER_NOT_AVAILABLE);
        }
    }

}