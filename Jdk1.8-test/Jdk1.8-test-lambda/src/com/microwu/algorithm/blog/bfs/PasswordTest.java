package com.microwu.algorithm.blog.bfs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Description: 解开密码锁的最少次数
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   10:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PasswordTest {

    /**
     * 将 s[j] 向上拨动一次
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  10:23
     *
     * @param   	s
     * @param 		j
     * @return  java.lang.String
     */
    String plusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '9') {
            ch[j] = '0';
        } else {
            ch[j] += 1;
        }
        return new String(ch);
    }

    /**
     * 将 s[i] 向下拨动一次
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  10:37
     *
     * @param   	s
     * @param 		j
     * @return  java.lang.String
     */
    String minusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '0') {
            ch[j] = '9';
        } else {
            ch[j] -= 1;
        }
        return new String(ch);
    }

    /**
     * 打印出所有可能的密码
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  10:37
     *
     * @param   	target
     * @return  void
     */
    void bfs(String target) {
        LinkedList<String> queue = new LinkedList<>();
        queue.offer("0000");

        while (!queue.isEmpty()) {
            int size = queue.size();
            // 将当前队列中的所有节点向周围扩散
            for (int i = 0; i < size; i++) {
                String cur = queue.poll();
                // 判断是否到达终点
                System.out.println(cur);

                // 将一个节点的相邻节点加入队列
                for (int j = 0; j < 4; i++) {
                    String up = plusOne(cur, i);
                    String down = minusOne(cur, i);
                    queue.offer(up);
                    queue.offer(down);
                }
            }
            // 在这里增加步数
        }
        return;
    }

    int openLock(String[] deadends, String target) {
        // 记录需要跳过的死亡密码
        Set<String> deads = new HashSet<>();
        for (String s : deadends) {
            deads.add(s);
        }
        // 记录过已经穷举过的密码，防止走回头路
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> q = new LinkedList<>();
        // 从起点开始启动广度优先搜索
        int step = 0;
        q.offer("0000");
        visited.add("0000");

        while (!q.isEmpty()) {
            int size = q.size();
            // 将当前队列中的所有节点向周围扩散
            for (int i = 0; i < size; i++) {
                String cur = q.poll();

                // 判断是否到达终点
                if (deads.contains(cur)) {
                    continue;
                }
                if (cur.equals(target)) {
                    return step;
                }

                // 将一个节点的未遍历相邻节点加入队列
                for (int j = 0; i < 4; j++) {
                    String up = plusOne(cur, j);
                    if (!visited.contains(up)) {
                        q.offer(up);
                        visited.add(up);
                    }

                    String down = minusOne(cur, j);
                    if (!visited.contains(down)) {
                        q.offer(down);
                        visited.add(down);
                    }
                }
            }
            // 增加步数
            step++;
        }
        // 没找到密码
        return -1;
    }

}