package com.microwu.algorithm.leetcode;

/**
 * Description: 删除排序链表中的重复元素
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   15:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode83 {
    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 迭代
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  15:45
     *
     * @param   	head
     * @return  com.microwu.algorithm.leetcode.LeetCode83.ListNode
     */
    public ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            if (cur.next.val == cur.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     * 递归思路：通过返回下一节点连接到上一节点，实现当前节点的删除
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  15:46
     *
     * @param   	head
     * @return  com.microwu.algorithm.leetcode.LeetCode83.ListNode
     */
    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        head.next = deleteDuplicates2(head.next);

        return head.val == head.next.val ? head.next : head;
    }
}