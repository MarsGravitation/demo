package com.microwu.structure;

/**
 * Description: 官方
 *  哨兵节点：在树和链表中被广泛用作伪头，伪尾，通常不保存任何数据
 *  使用伪头来简化插入和删除
 *
 * 插入节点
 *  找到要插入位置节点的前驱节点
 *  如果要在头部插入，则它的前驱节点就是伪头
 *  如果要在尾部插入节点，则前驱节点就是尾节点
 *
 *  通过改变 next 来插入节点
 *
 *  toAdd.next = pred.next
 *  pred.next = toAdd
 *
 * 删除和插入同样的道理
 *  找到要删除的节点的前驱节点
 *  通过改变 next 来删除节点
 *
 * get：
 *  从伪头结点开始，向前走 index + 1 步
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/15   13:52
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyLinkedList2 {

    int size;
    ListNode head;

    public MyLinkedList2() {
        size = 0;
        // 哨兵节点被用作伪头始终存在，这样结构中永远不为空，它将至少包含一个伪头
        head = new ListNode(0);
    }

    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    /**
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/15  14:00
     *
     * @param   	index
     * @param 		val
     * @return  void
     */
    public void addAtIndex(int index, int val) {
        if (index > size) {
            return;
        }

        if (index < 0) {
            index = 0;
        }

        ++size;
        ListNode pred = head;

        for (int i = 0; i < index; i++) {
            pred = pred.next;
        }

        ListNode toAdd = new ListNode(val);
        toAdd.next = pred.next;
        pred.next = toAdd;
    }

    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) {
            return;
        }

        size--;

        ListNode pred = head;
        for (int i = 0; i < index; i++) {
            pred = pred.next;
        }

        pred.next = pred.next.next;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            return -1;
        }

        ListNode curr = head;
        for (int i = 0; i < index + 1; i++) {
            curr = curr.next;
        }
        return curr.val;
    }

}