package com.microwu.structure.lru;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * https://crossoverjie.top/2018/04/07/algorithm/LRU-cache/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/16   10:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LruMap<K, V> {

    private final Map<K, V> cacheMap = new HashMap<>();

    /**
     * 最大缓存大小
     */
    private final int cacheSize;

    /**
     * 节点大小
     */
    private int nodeCount;

    /**
     * 头结点
     */
    private Node<K, V> head;

    /**
     * 尾节点
     */
    private Node<K, V> tail;

    public LruMap(int cacheSize) {
        this.cacheSize = cacheSize;
        // 头结点的下一个节点为空
        head = new Node<>();
        head.next = null;

        // 尾节点的上一个节点为空
        tail = new Node<>();
        tail.tail = null;

        // 双向链表，头结点的上节点指向尾节点
        head.tail = tail;

        // 尾节点的下节点指向头结点
        tail.tail = head;

    }

    public void put(K key, V value) {
        cacheMap.put(key, value);

        // 双向链表中添加节点
        addNode(key, value);
    }

    public V get(K key) {
        Node<K, V> node = getNode(key);
        
        // 移动到头结点
        moveToHead(node);
        
        return cacheMap.get(key);
    }

    private void moveToHead(Node<K,V> node) {
        // 如果是最后的一个节点
        if (node.tail == null) {
            node.next.tail = null;
            tail = node.next;
            nodeCount--;
        }

        // 如果是头结点，不作处理
        if (node.next == null) {
            return;
        }

        // 如果处于中间节点
        if (node.tail != null && node.next != null) {
            // 它的上一节点指向它的下一届点，也就是删除当前节点
            node.tail.next = node.next;
            nodeCount--;
        }

        // 最后在头部增加当前节点
        // 注意这里需要重新 new 一个对象，不然原本的 node 还有这下面的引用，会造成内存溢出
        node = new Node<>(node.key, node.value);
        addHead(node);
    }

    private Node<K,V> getNode(K key) {
        Node<K, V> node = tail;
        while (node != null) {
            if (node.key.equals(key)) {
                return node;
            }

            node = node.next;
        }
        return null;
    }

    private void addNode(K key, V value) {
        Node<K, V> node = new Node<>(key, value);

        // 容量满了删除最后一个
        if (cacheSize == nodeCount) {
            // 删除尾节点
            delTail();
        }

        // 写入头结点
        addHead(node);
    }

    private void addHead(Node<K,V> node) {
        // 写入头结点
        head.next = node;
        node.tail = head;
        head = node;
        nodeCount++;

        // 如果写入的数据大于两个，就将初始化的头尾节点删除
        if (nodeCount == 2) {
            tail.next.next.tail = null;
            tail = tail.next.next;

        }
    }

    private void delTail() {
        // 把尾节点从缓存中删除
        cacheMap.remove(tail.getKey());

        // 删除尾节点
        tail.next.tail = null;
        tail = tail.next;

        nodeCount--;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        Node<K, V> tail;
        Node<K, V> next;

        Node() {

        }

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getTail() {
            return tail;
        }

        public void setTail(Node<K, V> tail) {
            this.tail = tail;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

}