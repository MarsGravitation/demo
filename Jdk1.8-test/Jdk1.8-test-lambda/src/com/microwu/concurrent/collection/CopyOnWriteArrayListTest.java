package com.microwu.concurrent.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: CopyOnWriteArrayList
 *  优点：保证多线程的并发读写的线程安全
 *  确定：数组拷贝带来的内存问题，如果实际应用数据比较多，而且比较大的情况下，占用内存会比较大，考虑用 ConcurrentHashMap 代替
 *  如何避免：应为 CopyOnWrite 的写时复制机制，所以在进行写操作的时候，内存里会同时驻扎两个对象的内存，旧的对象和新写入的对象，
 *      注意：在复制的时候只是复制容器里的引用，只是再写的时候会创建新对象添加到新容器中，而就容器的对象还在使用，所以有两份对象
 *      内存。如果这些对象占用的内存较大，可能会造成频繁的 GC。
 *      针对内存占用问题，可以通过压缩容器中的元素的方法来减少大对象的内存消耗；或者使用其他的并发容器，比如 ConcurrentHashMap
 *  数据一致性：只保证数据的最终一致性，不能保证数据的实时一致性
 *  使用场景：读多写少的并发场景
 *
 * 源码：
 *  get：读的时候不需要加锁，如果读的时候多个线程正在向 ArrayList 添加数据，读还是会读到旧的数据
 *  add/set：使用 ReentrantLock 保证操作的线程安全性，然后复制整个数组 。
 *      复制的意义是让读的时候没有锁，提升读取的性能
 *      - Object[] newElements = Arrays.copyOf(elements, len + 1);
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   13:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
        list.add("a");
        list.get(0);
    }

    class CopyOnWriteMap<K, V> implements Map<K, V>, Cloneable {

        private volatile Map<K, V> internalMap;

        transient final ReentrantLock lock = new ReentrantLock();

        public CopyOnWriteMap() {
            internalMap = new HashMap<>();
        }

        @Override
        public int size() {
            return internalMap.size();
        }

        @Override
        public boolean isEmpty() {
            return internalMap.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return internalMap.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return internalMap.containsValue(value);
        }

        @Override
        public V get(Object key) {
            return internalMap.get(key);
        }

        @Override
        public V put(K key, V value) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                Map<K, V> newMap = new HashMap<>(internalMap);
                V val = newMap.put(key, value);
                internalMap = null;
                internalMap = newMap;
                return val;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public V remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<K> keySet() {
            return null;
        }

        @Override
        public Collection<V> values() {
            return null;
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            return null;
        }
    }
}