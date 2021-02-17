package com.microwu.structure.tree;

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

    class TreeNode {
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

}