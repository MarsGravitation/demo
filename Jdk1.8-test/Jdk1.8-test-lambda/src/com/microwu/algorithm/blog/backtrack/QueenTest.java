package com.microwu.algorithm.blog.backtrack;

import java.util.LinkedList;

/**
 * Description: N 皇后问题
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   9:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QueenTest {

    private LinkedList<LinkedList<String>> res;

    /**
     * 输入棋盘边长 n，返回所有合法的位置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  9:39
     *
     * @param   	n
     * @return  java.util.LinkedList<java.util.LinkedList<java.lang.String>>
     */
    private LinkedList<LinkedList<String>> solveQueens(int n) {
        // . 表示空，Q 表示皇后，初始化空棋盘
        LinkedList<String> board = new LinkedList<>();
        backtrack(board, 0);
        return res;
    }

    /**
     * 路径：board 中小于 row 的那些行都已经成功放置了皇后
     * 选择列表：第 row 行的所有列都是放置皇后的选择
     * 结束条件：row 超过 board 的最后一行
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  9:44
     *
     * @param   	board
     * @param 		row
     * @return  void
     */
    private void backtrack(LinkedList<String> board, int row) {
        if (row == board.size()) {
            res.add(board);
            return;
        }

        return;
    }

}