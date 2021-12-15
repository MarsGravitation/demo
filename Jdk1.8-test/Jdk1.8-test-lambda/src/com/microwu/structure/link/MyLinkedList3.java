package com.microwu.structure;

/**
 * Description: 双链表
 *  size
 *  伪头
 *  伪尾
 *
 *  伪头和伪尾总是存在，所有的节点都包含：值 + 向前的指针 + 向后的指针
 *
 *  找到要插入节点的前驱节点和后继节点。如果要在头部插入节点，则它的前驱节点是伪头。如果要在尾部插入节点，则它的后继结点是伪尾
 *  通过改变前驱节点和后继结点的链接关系添加元素
 *
 *  添加操作
 *      cur.prev = prev
 *      cur.next = prev.next;
 *      prev.next = cur;
 *      cur.next.prev = cur;
 *
 *  删除操作
 *      cur.prev.next = cur.next
 *      cur.next.prev = cur.prev
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/15   14:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyLinkedList3 {

    class ListNode {
        int val;
        ListNode prev;
        ListNode next;

        ListNode (int x) {
            val = x;
        }

    }

    int size;
    ListNode head;
    ListNode tail;

    public MyLinkedList3() {
        size = 0;
        head = new ListNode(0);
        tail = new ListNode(0);
        head.next = tail;
        tail.prev = head;
    }

    public void addAtIndex(int index, int val) {
        if (index > size) {
            return;
        }

        if (index < 0) {
            index = 0;
        }

        ListNode pred, succ;
        if (index < size - index) {
            pred = head;
            for (int i = 0; i < index; i++) {
                pred = pred.next;
            }
            succ = pred.next;
        } else {
            succ = tail;
            for (int i = 0; i < size - index; i++) {
                succ = succ.prev;
            }
            pred = succ.prev;
        }

        size++;
        ListNode toAdd = new ListNode(val);
        toAdd.prev = pred;
        toAdd.next = succ;
        pred.next = toAdd;
        succ.prev = toAdd;
    }

    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) {
            return;
        }

        ListNode pred, succ;
        if (index < size - index) {
            pred = head;
            for (int i = 0; i < index; i++) {
                pred = pred.next;
            }
            succ = pred.next.next;
        } else {
            succ = tail;
            for (int i = 0; i < size - index - 1; i++) {
                succ = succ.prev;
            }
            pred = succ.prev.prev;
        }

        size--;
        pred.next = succ;
        succ.prev = pred;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            return -1;
        }

        ListNode curr = head;
        if (index + 1 < size - index) {
            for (int i = 0; i < index + 1; i++) {
                curr = curr.next;
            }
        } else {
            curr = tail;
            for (int i = 0; i < size - index; i++) {
                curr = curr.prev;
            }
        }
        return curr.val;
    }

}