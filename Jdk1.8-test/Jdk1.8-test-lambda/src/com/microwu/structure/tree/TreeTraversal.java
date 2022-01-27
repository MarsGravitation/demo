package com.microwu.structure.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树的遍历
 *
 * DFS - 深度优先搜索
 * BFS - 广度优先搜索
 *
 *
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2022/1/27  15:27
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class TreeTraversal {

    private class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;
    }

    void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        dfs(root.right);
    }

    void bfs(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

}
