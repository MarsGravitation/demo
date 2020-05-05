package com.microwu.structure;

/**
 * Description: 使用链表实现 增删改
 * jdk 源码分析： https://github.com/wupeixuan/JDKSourceCode1.8/blob/master/src/java/util/ArrayList.java
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/26   10:04
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LinkedList {

    private int size = 0;

    private Node headerNode;

    private Node tailNode;

    private class Node {

        private int data;

        private Node nextNode;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node nextNode) {
            this.nextNode = nextNode;
        }

    }

    public void add(int num) {
        Node node = new Node();
        node.setData(num);
        if (size == 0 && headerNode == null) {
            // 添加第一个元素
            headerNode = node;
            tailNode = node;
        } else {
            // 添加后面的元素
            tailNode.nextNode = node;
            tailNode = node;
        }
        size++;
    }

    public void delete(int index) {
        if (index < 0) {
            throw new RuntimeException();
        }

        size--;

        // 删除头结点
        if (index == 0) {
            Node node = headerNode.nextNode;
            if (node != null) {
                headerNode = node;
            }
            headerNode = null;
            return;
        }

        // 删除中间节点
        int i = 0;
        Node node = headerNode;
        Node lastNode = null;
        while (i++ != index) {
            lastNode = node;
            node = node.nextNode;
        }
        lastNode.nextNode = node.nextNode;

    }

    public int index(int index) {
        Node node = headerNode;
        int i = 0;
        while (i++ != index) {
            node = node.nextNode;
        }
        return node.data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (size == 0 && headerNode == null) {
            return null;
        }

        Node node = headerNode;
        do {
            builder.append(node.data).append(",");
            node = node.nextNode;
        } while (node.nextNode != null);

        builder.append(node.data);
        return builder.toString();
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(0);
        linkedList.add(1);
        linkedList.add(2);
        System.out.println(linkedList.toString());
//        linkedList.delete(1);
//        System.out.println(linkedList.toString());
        System.out.println(linkedList.index(2));
    }
}