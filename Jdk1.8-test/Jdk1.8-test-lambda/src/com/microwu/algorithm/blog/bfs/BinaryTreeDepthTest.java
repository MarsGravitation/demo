package com.microwu.algorithm.blog.bfs;

import java.util.LinkedList;

/**
 * Description: 二叉树深度
 *  给定二叉树 [3, 9, 20, null, null, 15, 7]
 *  最小深度 2
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   10:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BinaryTreeDepthTest {

    class TreeNode {
        TreeNode left;
        TreeNode right;
    }

    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        LinkedList<TreeNode> q = new LinkedList<>();
        q.offer(root);
        // root 本身就是一层，depth 初始化为 1
        int depth = 1;

        while (!q.isEmpty()) {
            int sz = q.size();
            // 将当前队列中的所有节点向四周扩散
            for (int i = 0; i < sz; i++) {
                TreeNode cur = q.poll();
                // 判断是否到达了终点
                if (cur.left == null && cur.right == null) {
                    return depth;
                }
                if (cur.left != null) {
                    q.offer((cur.left));
                }
                if (cur.right != null) {
                    q.offer(cur.right);
                }
            }
            depth++;
        }
        return depth;
    }

}