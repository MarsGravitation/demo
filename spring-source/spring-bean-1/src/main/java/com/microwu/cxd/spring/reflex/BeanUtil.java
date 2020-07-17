package com.microwu.cxd.spring.reflex;

import com.microwu.cxd.spring.pojo.Person;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: cglib 的部分方法
 * https://www.iteye.com/topic/799827
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/7   10:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BeanUtil {

    public static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     * 将 map 转换成 bean
     *
     * 使用注意：
     *  1. 避免每次创建一个对象，一般建议是 setBean 动态替换持有的对象实例
     *  2. put，putAll 操作会直接修改 pojo 的属性，可以通过 putAll(map) 进行 map <-> bean 属性的拷贝
     *
     * 但是我感觉存在并发问题，所以还是每次都创建一个新的 beanMap
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/7  11:13
     *
     * @param   	map
     * @param 		bean
     * @return  T
     */
    public static <T> T map2Bean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * BeanCopier 拷贝速度快，性能瓶颈出现在创建 BeanCopier 实例的过程中
     *
     * 把创建过的实例放到缓冲中，提升性能
     *
     * https://www.iteye.com/blog/czj4451-2044150
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/7  11:35
     *
     * @param   	source
     * @param 		target
     * @return  void
     */
    public static void copy(Class<?> source, Class<?> target) {
        String key = genKey(source, target);
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(source, target, false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(source, target, null);
    }

    private static String genKey(Class<?> source, Class<?> target) {
        return source.getName() + "@" + target.getName();
    }

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "cxd");
        map.put("age", 25);
        Person person = map2Bean(map, new Person());
        System.out.println(person);
    }

}