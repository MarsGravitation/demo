package com.microwu.algorithm.blog.bfs;

/**
 * Description:
 *  DFS - 回溯算法
 *  BFS 和 DFS 的最主要区别是：BFS 找到的路径一定是最短的，但代价就是空间复杂度比 DFS 大很多
 *
 *  BFS 问题的本质就是让你在一副图中找到起点 start  到 终点 target 的最近距离
 *
 *  int BFS(Bode start, Node target) {
 *      // 核心数据结构
 *      Queue<Node> q;
 *      // 避免走回头路
 *      Set<Node> visited;
 *
 *      // 将起点加入队列
 *      q.offer(start);
 *      visited.add(start);
 *
 *      // 记录扩散的步数
 *      int step = 0;
 *
 *      while (q not empty) {
 *          int sz = q.size();
 *          // 将当前队列中的所有节点向四周扩散
 *          for (int i = 0; i < sz; i++) {
 *              Node cur = q.poll();
 *              // 这里判断是否到达终点
 *              if (cur is target)
 *                  return stop;
 *              // 将 cur 的相邻节点加入队列
 *              for (Node x : cur.adj())
 *                  if (x not in visited {
 *                      q.offer(x);
 *                      visited.add(x);
 *                  }
 *          }
 *          // 更新步数
 *          step++;
 *      }
 *  }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   9:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BfsTest {
}