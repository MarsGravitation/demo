package com.microwu.algorithm.niuke;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 反转列表
 *
 * 1. 递归
 * 2. 头插法
 * 3. 栈
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/7   11:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReverseList {

    class ListNode {
        int val;

        ListNode next;

        public ListNode() {

        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }
    }

    /**
     * 递归 - 自己的实现
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/7  11:58
     *
     * @param   	listNode
     * @param 		list
     * @return  void
     */
    public void recursive(ListNode listNode, List<ListNode> list) {
        if (listNode == null) {
            return;
        }
        recursive(listNode.next, list);
        list.add(listNode);

    }

    /**
     * 递归 - 别人的实现
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/7  13:57
     *
     * @param   	listNode
     * @return  java.util.ArrayList
     */
    public ArrayList recursive2(ListNode listNode) {
        ArrayList<Integer> list = new ArrayList<>();
        if (list != null) {
            list.addAll(recursive2(listNode.next));
            list.add(listNode.val);
        }
        return list;
    }

    /**
     * 头插法 - 自己实现
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/7  14:08
     *
     * @param   	listNode
     * @return  java.util.ArrayList<java.lang.Integer>
     */
    public ArrayList<Integer> test02(ListNode listNode) {
        if (listNode == null) {
            return null;
        }
        // 头结点，不存在任何值，辅助作用
        ListNode headNode = new ListNode();
        // 遍历原集合
        ListNode node = listNode;
        ListNode nextNode = listNode.next;
        while (node != null) {
            headNode.next = node;
            node.next =
            node = nextNode;
            nextNode = nextNode.next;
        }
        return null;
    }

    public static void main(String[] args) {

    }
}