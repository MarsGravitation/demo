package com.microwu.algorithm.blog.structure;

/**
 * Description: 数据结构
 *  1. 最高层的抽象，数据结构只有两种：数组和链表
 *      队列，栈可以使用链表，也可以使用数组实现
 *      图，邻接表就是链表，邻接矩阵就是二维数组
 *      散列表就是通过散列函数把键映射到一个数组中，散列冲突的方法：拉链法，线性探查法
 *      树，用数组实现就是完全二叉树，用链表实现就是很常见的树
 *  2. 数据结构的操作
 *      数组遍历 - 线性遍历结构
 *      for (int i = 0; i < arr.length; i++) {
 *
 *      }
 *
 *      二叉树遍历 - 非线性递归遍历结构
 *      void traverse(TreeNode root) {
 *          traverse(root.left)
 *          traverse(root.right)
 *      }
 *
 *      链表遍历
 *      void traverse(ListNode head) {
 *          for (ListNode p = head; p != null; p = p.next) {
 *
 *          }
 *      }
 *
 *      void traverse(ListNode head) {
 *          traverse(head.next)
 *      }
 *
 *      N 叉树遍历
 *      void traverse(TreeNode root) {
 *          for (TreeNode child : root.children) {
 *              traverse(child)
 *          }
 *      }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/6   13:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StructureTest {
}