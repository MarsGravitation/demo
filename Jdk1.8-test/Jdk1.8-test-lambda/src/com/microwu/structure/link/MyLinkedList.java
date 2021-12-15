package com.microwu.structure;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   16:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyLinkedList {

    class ListNode {
        private int val;
        private ListNode next;
    }

    private int size;
    private ListNode head;
    private ListNode tail;

    public MyLinkedList() {

    }

    /**
     * 获取链表中第 index 个节点的值。如果索引无效，则返回 -1
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  17:00
     *
     * @param   	index
     * @return  int
     */
    public int get(int index) {
        if (index >= size) {
            return -1;
        }
        ListNode node = head;
        int i = 0;
        while (i != index) {
            node = node.next;
            i++;
        }
        return node.val;
    }

    /**
     * 在链表的第一个元素之前添加一个值为 val 的节点。插入后，新节点将成为链表的第一个节点
     */
    public void addAtHead(int val) {
        ListNode node = new ListNode();
        node.val = val;
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    /** 将值为 val 的节点追加到链表的最后一个元素 */
    public void addAtTail(int val) {
        if (tail == null) {
            addAtHead(val);
        } else {
            ListNode node = new ListNode();
            tail.next = node;
            node.val = val;
            tail = node;
            size++;
        }
    }

    /**
     * 在链表中的第 index 个节点之前添加值为 val  的节点。
     * 如果 index 等于链表的长度，则该节点将附加到链表的末尾。
     * 如果 index 大于链表长度，则不会插入节点。
     * 如果index 小于 0，则在头部插入节点
     */
    public void addAtIndex(int index, int val) {
        if (index > size) {
            return;
        } else if (index == size) {
            addAtTail(val);
        } else if (index <= 0) {
            addAtHead(val);
        } else {
            int i = 1;
            ListNode pre = head;
            while (i != index) {
                pre = pre.next;
                i++;
            }
            ListNode node = new ListNode();
            node.val = val;
            ListNode next = pre.next;
            pre.next = node;
            node.next = next;
            size++;
        }
    }

    /** 如果索引 index 有效，则删除链表中的第 index 个节点 */
    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) {
            return;
        }
        // 删除头结点
        if (index == 0) {
            head = head.next;
        } else {
            ListNode prev = head;
            int i = 1;
            while (i != index) {
                prev = prev.next;
                i++;
            }
            if (prev.next.next == null) {
                // 最后一个节点
                tail = prev;
                prev.next = null;
            } else {
                prev.next = prev.next.next;
            }
        }
        size--;
    }

    @Override
    public String toString() {
        ListNode node = head;
        String s = "";
        while (node != null) {
            s += node.val + " -> ";
            node = node.next;
        }
        return s;
    }

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.addAtHead(1);
        list.addAtTail(3);
        list.addAtIndex(1, 2);
        System.out.println(list);
        System.out.println(list.get(1));
        list.deleteAtIndex(1);
        System.out.println(list);
        System.out.println(list.get(1));

    }
}