package com.microwu.structure;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/1   15:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BinaryTree {

    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    class Node {
        private Integer value;
        private Node leftChild;
        private Node rightChild;

        public Node() {

        }

        public Node(Integer value, Node leftChild, Node rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }


    }

    /**
     * 添加元素
     *
     * @param o
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/1  15:29
     */
    public void add(Integer o) {
        Node newNode = new Node(o, null, null);
        // 如果根节点为空，这个元素就是根节点
        if (root == null) {
            root = newNode;
            return;
        }
        /**
         * 自己写的
         *         Node node = root;
         *         while (node != null) {
         *             if (node.value > o) {
         *                 if ((node.leftChild) == null) {
         *                     node.leftChild = new Node(o, null, null);
         *                     return;
         *                 } else {
         *                     node = node.leftChild;
         *                 }
         *
         *             } else {
         *                 if ((node.rightChild) == null) {
         *                     node.rightChild = new Node(o, null, null);
         *                     return;
         *                 } else {
         *                     node = node.rightChild;
         *                 }
         *             }
         *         }
         *
         * 别人的写作思路：抽取出一个变量
         */
        Node current = root;
        Node parentNode = null;

        while (current != null) {
            parentNode = current;
            if (current.value > o) {
                // 左子叶
                current = parentNode.leftChild;
                if (current == null) {
                    parentNode.leftChild = newNode;
                    return;
                }

            } else {
                current = parentNode.rightChild;
                if (current == null) {
                    parentNode.rightChild = newNode;
                    return;
                }
            }
        }

    }

    /**
     * 查找元素
     *
     * @param i
     * @return boolean
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/1  15:46
     */
    public boolean find(Integer i) {
        Node node = root;
        while (node != null) {
            if (node.value.equals(i)) {
                return true;
            } else if (node.value > i) {
                node = node.leftChild;
            } else {
                node = node.rightChild;
            }
        }
        return false;
    }

    /**
     * 中序遍历 - 左中右
     *
     * @param node
     * @return java.lang.String
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/2  9:25
     */
    public String infixOrder(Node node) {
        StringBuilder builder = new StringBuilder();
        if (node == null) {
            return null;
        }

        if (node.leftChild != null) {
            String leftString = infixOrder(node.leftChild);
            builder.append(leftString).append(",");
        }

        builder.append(node.value).append(",");

        if (node.rightChild != null) {
            String rightString = infixOrder(node.rightChild);
            builder.append(rightString).append(",");
        }

        return builder.toString();

    }


    public void inOrderTraversal(Node current) {
        if (current != null) {
            inOrderTraversal(current.leftChild);
            System.out.print(current.value + " ");
            inOrderTraversal(current.rightChild);
        }
    }

    public void print() {
        inOrderTraversal(root);
    }


    @Override
    public String toString() {
        return infixOrder(root);
    }


    public static void main(String[] args) {

        BinaryTree binaryTree = new BinaryTree();
        binaryTree.add(4);
        binaryTree.add(6);
        binaryTree.add(2);
        binaryTree.add(1);
        binaryTree.add(3);

        System.out.println(binaryTree);
        binaryTree.print();
        System.out.println();

        System.out.println(binaryTree.find(5));
    }
}