package com.microwu.algorithm.leetcode;

/**
 * Description: 反转链表
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/4   15:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode206 {

    static class Node {
        private Node head;
        private int value;
        private Node next;
        private Node tail;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void add(int value) {
            Node node = new Node();
            node.value = value;
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }

        @Override
        public String toString() {
            if (head == null) {
                return "";
            }
            Node node = head;
            String s = "";
            while (node.next != null) {
                s += (node.value + " ---> ");
                node = node.next;
            }

            s += tail.value;
            return s;
        }
    }

    /**
     * 1. 定义一个函数 reverse
     * 2. 问题和子问题的关系，从上而下
     *  |- 如果要反转链表，先要反转链表 n - 1
     *  3. 临界条件
     *  4. 公式
     *      node(n - 1) = node(n).next
     *      node(n - 1).next = node(n)
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  15:09
     *
     * @param   	head
     * @return  void
     */
    public void reverse(Node head) {
        if (head == null || head.next == null) {
            return;
        }
        reverse(head.next);

        Node next = head.next;
        next.next = head;
        head.next = null;
    }

    public static void main(String[] args) {
        Node node = new Node();
        node.add(1);
        node.add(2);
        node.add(3);
        System.out.println(node.toString());

        LeetCode206 code206 = new LeetCode206();
        code206.reverse(node.head);
        System.out.println(node.toString());
    }

}