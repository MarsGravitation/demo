package com.microwu.structure.link;

import java.util.StringJoiner;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/8/12  10:31
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class MyLinkedList4<E> {

    private int size;
    private final Node<E> head;

    /**
     *  哨兵节点被用作伪头始终存在，这样结构中永远不为空，它将至
     *  少包含一个伪头。
     *
     *  Node 结构：值 + 链接到下一个元素的指针
     */
    public MyLinkedList4() {
        this.head = new Node<E>(null);
    }

    public void addAtHead(E e) {
        addAtIndex(0, e);
    }

    public void addAtTail(E e) {
        addAtIndex(size, e);
    }

    /**
     * addAtIndex，因为伪头的关系 addAtHead 和 addAtTail
     * 可以使用 addAtIndex 来完成
     *
     * 找到要插入位置节点的前驱节点。如果要在头部插入，则
     * 它的前驱节点就是伪头。如果要在尾部插入节点，则前驱
     * 节点就是尾节点
     *
     * 通过改变 next 来插入节点
     *
     * toAdd.next = pred.next;
     * pred.next = toAdd;
     *
     *     ++size;
     *     // find predecessor of the node to be added
     *     ListNode pred = head;
     *     for(int i = 0; i < index; ++i) pred = pred.next;
     *
     *     // node to be added
     *     ListNode toAdd = new ListNode(val);
     *     // insertion itself
     *     toAdd.next = pred.next;
     *     pred.next = toAdd;
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/12     11:09
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
        int i;
        Node<E> preNode = head;
        for (i = 0; i < index; i++) {
            preNode = preNode.next;
        }
        Node<E> nextNode = preNode.next;
        Node<E> node = new Node<>(e);
        preNode.next = node;
        node.next = nextNode;
    }

    /**
     * 找到要删除节点的前驱节点
     *
     * 通过改变 next 来删除节点
     *
     * pred.next = pred.next.next
     *
     *      size--;
     *     // find predecessor of the node to be deleted
     *     ListNode pred = head;
     *     for(int i = 0; i < index; ++i) pred = pred.next;
     *
     *     // delete pred.next
     *     pred.next = pred.next.next;
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/12     11:08
     *
     * @param index
     * @return E
     */
    public E deleteAtIndex(int index) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        size--;
        Node<E> preNode = head;
        Node<E> curNode = head.next;
        for (int i = 0; i < index; i++) {
            preNode = preNode.next;
            curNode = curNode.next;
        }
        preNode.next = curNode.next;
        return curNode.value;
    }

    private static class Node<E> {
        private E value;
        private Node<E> next;

        public Node(E e) {
            this.value = e;
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        Node<E> curNode = head.next;
        for (int i = 0; i < size; i++) {
            stringJoiner.add(curNode.value.toString());
            curNode = curNode.next;
        }
        return stringJoiner.toString();
    }

    public static void main(String[] args) {
        MyLinkedList4<Integer> list = new MyLinkedList4<Integer>();
        list.addAtTail(1);
        list.addAtTail(2);
        list.addAtTail(3);
        System.out.println(list);
        list.deleteAtIndex(0);
        System.out.println(list);
    }

}
