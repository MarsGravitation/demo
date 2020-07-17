package com.microwu.cxd.jpa.utils;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/17   16:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EntityManagerUtil {

    public static List findRelatives(String sql, Map<String, Object> params, EntityManager entityManager, Class<?> clazz) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(clazz));
//        Query query = entityManager.createNativeQuery(sql, clazz);
        for (String key : params.keySet()) {
            Object value = params.get(key);
            query.setParameter(key, value);
        }
        entityManager.close();
        return query.getResultList();
    }

}