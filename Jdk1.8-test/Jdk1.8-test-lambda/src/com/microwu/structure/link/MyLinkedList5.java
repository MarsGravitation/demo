package com.microwu.structure.link;

import java.util.StringJoiner;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/8/12  11:12
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class MyLinkedList5<E> {

    private int size;
    private final Node<E> head;
    private Node<E> tail;

    /**
     * 双链表比单链表快得多
     *
     * 它包含了 size，记录链表元素个数，和伪头伪尾
     *
     * 伪头和伪尾总是存在
     *
     * Node：值 + 指向前一个节点的指针 + 指向后一个节点的指针
     */
    public MyLinkedList5() {
        head = new Node<>(null);
        tail = new Node<>(null);

        head.next = tail;
        tail.pre = head;
    }

    /**
     * 找到要插入节点的前驱节点和后继节点。如果要在头部插
     * 入节点，则它的前驱节点是伪头。如果要在尾部插入节
     * 点，则它的后继结点就是伪尾
     *
     * 通过改变前驱节点和后继节点的链接关系添加元素
     *
     * toAdd.prev = pred;
     * toAdd.next = succ;
     * pred.next = toAdd;
     * succ.prev = toAdd;
     *
     *   // find predecessor and successor of the node to be added
     *     ListNode pred, succ;
     *     if (index < size - index) {
     *       pred = head;
     *       for(int i = 0; i < index; ++i) pred = pred.next;
     *       succ = pred.next;
     *     }
     *     else {
     *       succ = tail;
     *       for (int i = 0; i < size - index; ++i) succ = succ.prev;
     *       pred = succ.prev;
     *     }
     *
     *     // insertion itself
     *     ++size;
     *     ListNode toAdd = new ListNode(val);
     *     toAdd.prev = pred;
     *     toAdd.next = succ;
     *     pred.next = toAdd;
     *     succ.prev = toAdd;
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/12     14:15
     *
     * @param index
     * @param e
     * @return void
     */
    public void addAtIndex(int index, E e) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        size++;
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        Node<E> addNode = new Node<>(e);
        node.next.pre = addNode;
        addNode.next = node.next;
        node.next = addNode;
        addNode.pre = node;
    }

    public void addAtTail(E e) {
        addAtIndex(size, e);
    }

    /**
     * 找到要删除节点的前驱节点和后继节点
     *
     * 通过改变前驱节点和后继结点的链接关系删除元素
     *
     * pred.next = succ;
     * succ.prev = pred;
     *
     *
     *     // find predecessor and successor of the node to be deleted
     *     ListNode pred, succ;
     *     if (index < size - index) {
     *       pred = head;
     *       for(int i = 0; i < index; ++i) pred = pred.next;
     *       succ = pred.next.next;
     *     }
     *     else {
     *       succ = tail;
     *       for (int i = 0; i < size - index - 1; ++i) succ = succ.prev;
     *       pred = succ.prev.prev;
     *     }
     *
     *     // delete pred.next
     *     --size;
     *     pred.next = succ;
     *     succ.prev = pred;
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/12     14:18
     *
     * @param index
     * @return E
     */
    public E removeAtIndex(int index) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        size--;
        Node<E> curNode = head.next;
        for (int i = 0; i < index; i++) {
            curNode = curNode.next;
        }
        curNode.pre = curNode.next;
        curNode.next.pre = curNode.pre;
        return curNode.e;
    }

    private static class Node<E> {
        private final E e;
        private Node<E> pre;
        private Node<E> next;

        public Node(E e) {
            this.e = e;
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        Node<E> curNode = head.next;
        for (int i = 0; i < size; i++) {
            stringJoiner.add(curNode.e.toString());
            curNode = curNode.next;
        }
        return stringJoiner.toString();
    }

    public static void main(String[] args) {
        MyLinkedList5<Integer> list = new MyLinkedList5<Integer>();
        list.addAtTail(1);
        list.addAtTail(2);
        list.addAtTail(3);
        System.out.println(list);
        list.removeAtIndex(0);
        System.out.println(list);
    }
}
