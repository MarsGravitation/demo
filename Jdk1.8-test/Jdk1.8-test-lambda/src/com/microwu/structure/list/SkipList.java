package com.microwu.structure.list;

/**
 *
 * 跳表：
 * http://zhangtielei.com/posts/blog-redis-skiplist.html
 *
 * 实现：
 * https://leetcode-cn.com/problems/design-skiplist/solution/javashou-xie-shi-xian-tiao-biao-by-feng-omdm0/
 *
 * 思路：
 *  1. 先随机出来一个层数，new 要插入的节点 - newNode
 *  2. 根据跳表实际的总层数从上往下分析，要插入一个节点 newNode 时，先找到节点
 *  在该层的位置：因为是链表，所以需要一个节点 node，满足插入节点 newNode 的值
 *  刚好不大于 node 的下一个节点值，或者，下个节点为空
 *  3. 确定插入节点 newNode 在该层的位置后，先判断下 newNode 的随机层数是否
 *  小于当前跳表的总层数，如果是，则用链表的插入方法将 newNode 插入即可
 *  4. 如此循环，直到最底层插入 newNode 完毕
 *  5. 循环完毕后，还需要判断下 newNode 随机出来的层数是否要比跳表的实际层数
 *  还要大，如果是，直接将超过实际层数的跳表的投机诶点指向 newNode 即可，改
 *  跳表的实际层数就变为 newNode 的随机层数了
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2022/2/7  16:30
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class SkipList {

    /**
     * 最大层数
     */
    private static final int DEFAULT_MAX_LEVEL = 32;

    /**
     * 随机层数概率，也就是随机出的层数，在 第一层以上（不包括第一层）的概率，层数不超过 maxLevel，层数的起始号为 1
     */
    private static final double DEFAULT_P_FACTOR = 0.25;

    /**
     * 头节点
     */
    Node head = new Node(null, DEFAULT_MAX_LEVEL);

    /**
     * 表示当前 nodes 的实际层数，它从 1 开始
     */
    int currentLevel = 1;

    public SkipList() {

    }

    /**
     * 随机一个层数
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/2/7     16:37
     *
     * @param
     * @return int
     */
    private static int randomLevel() {
        int level = 1;
        while (Math.random() < DEFAULT_P_FACTOR && level < DEFAULT_MAX_LEVEL) {
            level++;
        }
        return level;
    }

    /**
     * 找到 level 层 value 刚好不小于 node 的节点
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/2/7     16:47
     *
     * @param node 从哪个节点开始找
     * @param levelIndex 所在层
     * @param value 要插入的节点值
     * @return com.microwu.structure.list.SkipList.Node
     */
    private Node findClosest(Node node, int levelIndex, int value) {
        while (node.next[levelIndex] != null && value > node.next[levelIndex].value) {
            node = node.next[levelIndex];
        }
        return node;
    }

    /**
     * 查询
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/2/7     17:16
     *
     * @param target
     * @return boolean
     */
    public boolean search(int target) {
        Node searchNode = head;
        for (int i = currentLevel - 1; i >= 0; i--) {
            searchNode = findClosest(searchNode, i, target);
            if (searchNode.next[i] != null && searchNode.next[i].value == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/2/8     10:08
     *
     * @param num
     * @return void
     */
    public void add(int num) {
        int level = randomLevel();
        System.out.println(level + "," + num);
        Node updateNode = head;
        Node newNode = new Node(num, level);
        // 计算出当前 num 索引的实际层数，从该层开始添加索引
        for (int i = currentLevel - 1; i >= 0; i--) {
            // 找到本层最近离 num 最近的 list
            updateNode = findClosest(updateNode, i, num);
            if (i < level) {
                if (updateNode.next[i] == null) {
                    updateNode.next[i] = newNode;
                } else {
                    Node temp = updateNode.next[i];
                    updateNode.next[i] = newNode;
                    newNode.next[i] = temp;
                }
            }
        }
        if (level > currentLevel) {
            for (int i = currentLevel; i < level; i++) {
                head.next[i] = newNode;
            }
            currentLevel = level;
        }
    }

    /**
     * 擦除
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/2/7     17:19
     *
     * @param num
     * @return boolean
     */
    public boolean erase(int num) {
        boolean flag = false;
        Node node = head;
        for (int i = currentLevel - 1; i >= 0; i--) {
            node = findClosest(node, i , num);
            if (node.next[i] != null && node.next[i].value == num) {
                flag = true;
                node.next[i] = node.next[i].next[i];
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = currentLevel - 1; i >= 0; i--) {
            Node node = head;
            sb.append("null-->");
            while ((node = node.next[i]) != null) {
                sb.append(node.value).append("-->");
            }
            sb.append("null").append("\r\n");
        }
        return sb.toString();
    }

    private static class Node {
        /**
         * 节点值
         */
        Integer value;
        /**
         * 节点在不同层的下一个节点
         */
        Node[] next;

        public Node(Integer value, int size) {
            this.value = value;
            this.next = new Node[size];
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList();

        skipList.add(1);
        skipList.add(2);
        skipList.add(3);
        skipList.add(4);
        skipList.add(5);
        System.out.println(skipList);
    }

}
