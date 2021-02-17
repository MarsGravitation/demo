package com.microwu.algorithm.leetcode;

import java.util.HashSet;

/**
 * Description: 环形链表
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   15:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode141 {

    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * 使用 HashSet 进行存储
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  15:34
     *
     * @param   	head
     * @return  boolean
     */
    public boolean hasCycle(ListNode head) {
        HashSet<ListNode> seen = new HashSet<>();
        while (head != null) {
            if (!seen.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /**
     * 快慢指针
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  15:34
     *
     * @param   	head
     * @return  boolean
     */
    public boolean hasCycle2(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;

    }

}