package com.microwu.structure.tree;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Description: 二叉树
 *  前序遍历：首先访问根节点，然后遍历左子树，最后遍历右子树
 *  中序遍历：先遍历左子树，根节点，右子树，可以得到一个递增的有序序列
 *  后续遍历：先遍历左子树，右子树，根节点
 *
 *  层序遍历：广度优先搜索，先访问根节点，然后遍历它的相邻节点，然后。。。
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/15   15:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BinaryTree2 {

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 前序遍历
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/15  15:41
     *
     * @param   	root
     * @return  void
     */
    public void preOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println(root.val);
        preOrder(root.left);
        preOrder(root.right);
    }

    private void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }

        inOrder(root.left);
        System.out.println(root.val);
        inOrder(root.right);
    }

    private void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }

        postOrder(root.left);
        postOrder(root.right);
        System.out.println(root.val);
    }

    /**
     * 广度优先搜索
     *  - 根元素入队
     *  - 当队列不为空的时候
     *      |- 求当前队列的长度 s(i)
     *      |- 依次从队列中取 s(i) 个元素进行拓展，然后进入下一次迭代
     *
     * 循环不变式：第 i 次迭代前，队列中所有元素就是第 i 层的所有元素，
     * 并且按照从左向右的顺序排列
     *  - 初始化：i = 1，队列里面只有 root
     *  - 保持：如果 i = k 时性质成立，即第 k 轮中出队 s(k) 的元素
     *  是第 k 层的所有元素，并且顺序从左到右。因为对树进行广度优先
     *  搜索的时候由低 k 层的点拓展出的点一定也只能是 k + 1 层的点，
     *  并且 k + 1 层的点只能由第 k 层的点拓展
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/1/27     15:17
     *
     * @param root
     * @return java.util.List<java.util.List<java.lang.Integer>>
     */
    private List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return null;
        }
        List<List<Integer>> ret = new ArrayList<>();

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            ArrayList<Integer> level = new ArrayList<>();
            int currentLevelSize = queue.size();
            for (int i = 0; i < currentLevelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            ret.add(level);
        }
        return ret;
    }

}