package com.microwu.concurrent.local;

/**
 * Description: ThreadLocal
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/31   16:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ThreadLocalTest {

    /**
     * ThreadLocal 当我们在创建一个变量后，如何每个线程对其进行访问的时候都访问的线程自己的变量，就不会出现线程安全问题
     *
     * ThreadLocal 它提供线程本地变量，如果创建一个 ThreadLocal 变量，那么访问这个变量的每个线程都会有这个变量的副本，实际
     * 多线程操作时，操作的是自己本地内存中的变量
     *
     * Thread 有两个变量 threadLocals 和 inheritableThreadLocals，二者都是 ThreadLocal 内部类 ThreadLocalMap 类型变量。
     * ThreadLocalMap 类似一个 HashMap。默认情况下，每个线程中的这两个变量都为 null，只有当线程第一次调用 ThreadLocal 的
     * set 或者 get 方法才会创建它们。每个线程的本地变量不是存放在 ThreadLocal 实例中，而是放在调用线程的 ThreadLocals 变量里边，
     * 也就是 Thread 类的变量。也就是说，ThreadLocal 类型的本地变量是存放在具体的线程空间上，其本身相当于一个装载本地变量的工具壳，
     * 通过 set 方法将 value 添加到调用线程 threadLocals 中，当调用线程用 get 方法时候能够从它的 threadLocals 中取出变量。如果调用
     * 线程一直不终止，那么这个本地变量将会一直存放在它的 threadLocals 中，所以不使用本地变量的时候需要调用 remove 方法将 threadLocals
     * 删除不用的本地的变量
     *
     * https://www.cnblogs.com/fsmly/p/11020641.html
     */
    private static ThreadLocal<String> localVar = new ThreadLocal<>();

    /**
     *  在并发编程的时候，成员变量如果不做任何处理其实是线程不安全的，各个线程都在操作同一个变量，显然是不行的，并且我们也知道 volatile
     *  这个关键字也是不能保证线程安全的。那么，在有一种情况之下，我们需要满足这样一个条件：变量是同一个，但是每个线程都是用同一个初始值，
     *  也就是使用同一个变量的一个新的副本。这种情况下 ThreadLocal 就非常适用，比如说 DAO 的数据库连接，我们知道 DAO 是单例的，那么它的属性
     *  Connection 就不是一个线程安全的变量。而我们每个线程都需要使用它，并且各自使用各自的。这种情况，ThreadLocal 就很好解决
     *
     *  应用场景：当很多线程需要多次使用同一个对象，并且需要该对象具有相同初始化值的时候
     *
     * https://www.cnblogs.com/dreamroute/p/5034726.html
     */
    public static class ConnectionUtil {
        private static final ThreadLocal<Connection> conn = new ThreadLocal<>();

        public static Connection getConn() {
            Connection con = conn.get();
            if (con == null) {
                try {
                    Class<?> clazz = Class.forName("com.microwu.concurrent.local.ThreadLocalTest.Connection");
                    con = (Connection) clazz.newInstance();
                    conn.set(con);

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return con;
        }
    }

    public class Connection {

    }


    public static void main(String[] args) {
        new Thread(() -> {
            localVar.set("localVar1");
            String s = localVar.get();
            System.out.println(s);

            localVar.set("localVar2");
            s = localVar.get();
            System.out.println(s);
        }).start();

    }
}