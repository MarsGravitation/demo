package com.microwu.structure.link;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/8/12  10:31
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class MyLinkedList4<E> {

    private int size;
    private Node<E> head;

    public MyLinkedList4() {
        this.head = new Node<E>(null);
    }

    public void addAtIndex(int index, E e) {

    }

    private static class Node<E> {
        private E value;
        private Node<E> next;

        public Node(E e) {
            this.value = e;
        }
    }

}
