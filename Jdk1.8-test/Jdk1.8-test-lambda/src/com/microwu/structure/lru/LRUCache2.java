package com.microwu.structure.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用 LinkedHashMapLinkedHashMap
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/8/13  10:38
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class LRUCache2 {

    private int capacity;
    Map<Integer, Integer> map;

    public LRUCache2(int capacity) {
        this.capacity = capacity;
        this.map = new LinkedHashMap<>();
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        // 先删除旧的位置，再放入新位置
        Integer value = map.remove(key);
        map.put(key, value);
        return value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            map.remove(key);
            map.put(key, value);
        } else {
            map.put(key, value);
            if (map.size() > capacity) {
                map.remove(map.entrySet().iterator().next().getKey());
            }
        }
    }

    class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        /**
         * 定义缓存的容量
         */
        private int capacity;

        public LRULinkedHashMap(int capacity) {
            super(16, 0.75f, true);
            // 传入指定的缓存最大容量
            this.capacity = capacity;
        }

        /**
         * 如果此映射应删除其最旧的条目，则返回 true。在将新条目插入地图后， put 和 putAll 会调用此方法。
         * 它为实现者提供了每次添加新条目时删除最旧条目的机会。如果映射代表缓存，这很有用：它允许映射通过删除
         * 陈旧条目来减少内存消耗。
         *
         * 使用示例：此覆盖将允许映射最多增加 100 个条目，然后在每次添加新条目时删除最旧的条目，保持 100 个条目的稳定状态。
         *
         * @author   chengxudong             chengxd2@lenovo.com
         * @date 2021/8/13     10:51
         *
         * @param eldest
         * @return boolean
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > capacity;
        }
    }

}
