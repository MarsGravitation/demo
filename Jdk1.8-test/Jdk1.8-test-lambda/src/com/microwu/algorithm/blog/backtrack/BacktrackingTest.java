package com.microwu.algorithm.blog.backtrack;

import java.util.LinkedList;
import java.util.List;

/**
 * Description: 回溯算法
 *  解决一个回溯问题，实际上就是一个决策树的遍历过程。
 *      1. 路径：也就是已经做出的的选择
 *      2. 选择列表：也就是你当前可以做的选择
 *      3. 结束条件：也就是到达决策树底层，无法再做选择的条件
 *
 *  回溯算法框架：
 *      result = []
 *      def backtrack(路径, 选择列表):
 *          if 满足结束条件：
 *          result.add(路径)
 *          return
 *      for 选择 in 选择列表:
 *          做选择
 *          backtrack(路径, 选择列表)
 *          撤销选择
 *
 *  其核心就是 for 循环里面的递归，在递归调用之前做选择，在递归调用之后撤销选择
 *
 *  问题：
 *      1. 全排列问题：给三个数 [1, 2, 3]
 *
 *  多叉树的遍历框架
 *      void traverse(TreeNode root) {
 *          for(TreeNode child : root.children) {
 *              // 前序遍历需要的操作
 *              traverse(child)
 *              // 后续遍历需要的操作
 *          }
 *      }
 *
 *  前序遍历的代码在进入某个节点之前的那个时间点执行，后续遍历代码在离开某个节点之后的那个时间点执行
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/4   11:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BacktrackingTest {
    private List<List<Integer>> res = new LinkedList<>();

    /**
     * 路径：记录在 track 中
     * 选择列表：nums 中不存在于 track 的那些元素
     * 结束条件：nums 中的元素全都在 track 中出现
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  16:12
     *
     * @param   	nums
     * @param 		track
     * @return  void
     */
    public void backtrack(int[] nums, LinkedList<Integer> track) {
        if (track.size() == nums.length) {
            res.add(new LinkedList<>(track));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (track.contains(nums[i])) {
                continue;
            }

            // 做选择
            track.add(nums[i]);
            // 进入下一层决策树
            backtrack(nums, track);
            // 取消选择
            track.removeLast();
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        LinkedList<Integer> track = new LinkedList<>();
        BacktrackingTest test = new BacktrackingTest();
        test.backtrack(nums, track);
        System.out.println(test.res);
    }

}