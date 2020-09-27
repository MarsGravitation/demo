package com.microwu.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: Java 中 try、catch、finally 带 return 的执行顺序
 *  异常处理中，try、catch、finally 的执行顺序。即，如果 try 中没有异常，顺序为 try -> finally
 *  如果 try 中有异常，则顺序为 try -> catch -> finally
 *  当 try、catch、finally 加入 return 后，会有几种不同的情况出现
 *
 * https://www.cnblogs.com/pcheng/p/10968841.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/29   13:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReturnTest {

    /**
     * try 中带有 return
     *
     * try: 2
     * finally: 3
     * result: 2
     *
     * 当 try 中带有 return 时，会先执行 return 前的代码，然后暂时保存需要 return 的信息，在执行 finally 中的代码，
     * 最后再通过 return 返回之前保存的信息
     *
     * 总结：
     *  1. finally 中的代码总会被执行
     *  2. 当try、catch 中有 return 时，也会执行 finally。 return 的时候，要注意返回值的类型，是否收到 finally 中代码的影响
     *  3. finally 中有 return 时，会直接在 finally 中退出，导致 try、catch 中 的 return 失效
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  13:43
     *
     * @param
     * @return  int
     */
    private static int testReturn1() {
        int i = 1;
        try {
            i++;
            System.out.println("try: " + i);
            return i;
        } catch (Exception e) {
            i++;
            System.out.println("catch: " + i);
        } finally {
            i++;
            System.out.println("finally: " + i);
        }
        return i;
    }

    /**
     * 引用类型的测试
     *
     * try: [1]
     * finally: [1, 3]
     * result: [1, 3]
     *
     * 这是因为上一个例子用的是基本类型，这里用的是引用类型。
     * 存储的不是变量本省，而是变量的地址，所以当 finally 通过地址改变了变量，还是会影响方法的返回值
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  13:49
     *
     * @param
     * @return  java.util.List<java.lang.Integer>
     */
    public static List<Integer> testReturn2() {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list.add(1);
            System.out.println("try: " + list);
            return list;
        } catch (Exception e) {
            list.add(2);
            System.out.println("catch: " + list);
        } finally {
            list.add(3);
            System.out.println("finally: " + list);
        }
        return list;
    }

    /**
     * catch 中带有 return
     *
     * try: 2
     * catch: 3
     * finally: 4
     * result 3
     *
     * catch 中 return 与 try 中一样，会先执行 return 前的代码，然后暂时保存需要 return 的信息
     * 再执行 finally 中的代码，最后再通过 return 返回之前保存的信息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  13:54
     *
     * @param
     * @return  int
     */
    private static int testReturn3() {
        int i = 1;
        try {
            i++;
            System.out.println("try: " + i);
            int x = 1 / 0;
        } catch (Exception e) {
            i++;
            System.out.println("catch: " + i);
            return i;
        } finally {
            i++;
            System.out.println("finally: " + i);
        }
        return i;
    }

    /**
     * finally 带有 return
     *
     * try: 2
     * finally: 3
     * result: 3
     *
     * 当 finally 中有 return 的时候， try 中的 return 会失效，在执行完 finally 的 return 之后，
     * 就不会再执行 try 中的 return
     * 这种写法，编译可以通过，但是不推荐，这会破坏程序的完整性，而且一旦 finally 里出现异常，会导致
     * catch 中的异常被覆盖
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  14:14
     *
     * @param
     * @return  int
     */
    private static int testReturn4() {
        int i = 1;
        try {
            i++;
            System.out.println("try: " + i);
            return i;
        } catch (Exception e) {
            i++;
            System.out.println("catch: " + i);
            return i;
        } finally {
            i++;
            System.out.println("finally: " + i);
            return i;

        }
    }

    /**
     * try 或 catch 中的 return 不可能同时执行
     * try 或 catch 中的 return 会优于函数最后的 return 执行
     *
     * try:2
     * catch:3
     * finally:4
     * result: 3
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  14:21
     *
     * @param   	
     * @return  int
     */
    private static int testReturn5() {
        int i = 1;
        try {
            i++;
            System.out.println("try:" + i);
            int x = i / 0 ;
            System.out.println("reach");
            // return i;
        } catch (Exception e) {
            i++;
            System.out.println("catch:" + i);
            return i;
        } finally {
            i++;
            System.out.println("finally:" + i);
            // return i;
        }
        return 5;
    }

    /**
     * 模拟 fmp 下载出现的异常
     *
     * i = 1
     * Exception: 2
     * result: 2
     *
     * i = 0
     * RuntimeException: 1
     * result: 1
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/29  14:36
     *
     * @param   	i
     * @return  int
     */
    public static int testReturn6(int i) {
        FileInputStream fileInputStream = null;
        try {
            if (i == 0) {
                int x = 1 / 0;
            }
            File file = new File("");
            fileInputStream = new FileInputStream(file);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println("result: " + testReturn1());
//        System.out.println("result: " + testReturn2());
//        System.out.println("result " + testReturn3());
//        System.out.println("result: " + testReturn4());
//        System.out.println("result: " + testReturn5());
        System.out.println("result: " + testReturn6(0));
    }

}